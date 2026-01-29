package commandservices

import (
	"context"
	"errors"
	"fmt"
	"net/url"
	"strings"

	"go-service/internal/payments/domain/model/commands"
	"go-service/internal/payments/domain/model/entities"
	"go-service/internal/payments/domain/model/valueobjects"
	"go-service/internal/payments/domain/repositories"
	"go-service/internal/payments/domain/services"
	"go-service/internal/payments/infrastructure/mercadopago"
	"go-service/internal/products/interfaces/acl"

	"github.com/google/uuid"
)

type paymentCommandServiceImpl struct {
	paymentRepo     repositories.PaymentRepository
	mpClient        *mercadopago.Client
	productFacade   acl.ProductPaymentFacade
	frontendBaseURL string
	notificationURL string
}

func buildURL(baseURL, path string) (string, error) {
	baseURL = strings.TrimSpace(baseURL)
	if baseURL == "" {
		return "", errors.New("base URL cannot be empty")
	}

	u, err := url.Parse(baseURL)
	if err != nil {
		return "", fmt.Errorf("invalid base URL %q: %w", baseURL, err)
	}
	if u.Scheme == "" || u.Host == "" {
		return "", fmt.Errorf("invalid base URL %q: scheme and host are required (example: https://example.com)", baseURL)
	}

	ref, err := url.Parse(path)
	if err != nil {
		return "", fmt.Errorf("invalid path %q: %w", path, err)
	}
	return u.ResolveReference(ref).String(), nil
}

func NewPaymentCommandService(
	paymentRepo repositories.PaymentRepository,
	mpClient *mercadopago.Client,
	productFacade acl.ProductPaymentFacade,
	frontendBaseURL string,
	notificationURL string,
) services.PaymentCommandService {
	return &paymentCommandServiceImpl{
		paymentRepo:     paymentRepo,
		mpClient:        mpClient,
		productFacade:   productFacade,
		frontendBaseURL: frontendBaseURL,
		notificationURL: notificationURL,
	}
}

func (s *paymentCommandServiceImpl) HandleCreate(ctx context.Context, cmd commands.CreatePaymentCommand) (*valueobjects.PaymentID, error) {
	product, err := s.productFacade.FetchProductForPayment(ctx, cmd.ProductID())
	if err != nil {
		return nil, err
	}

	paymentUUID := uuid.New()

	successURL, err := buildURL(s.frontendBaseURL, "/payments/success")
	if err != nil {
		return nil, err
	}
	failureURL, err := buildURL(s.frontendBaseURL, "/payments/failure")
	if err != nil {
		return nil, err
	}
	pendingURL, err := buildURL(s.frontendBaseURL, "/payments/pending")
	if err != nil {
		return nil, err
	}

	total := product.Price * float64(cmd.Quantity())
	amountVO, err := valueobjects.NewAmount(total)
	if err != nil {
		return nil, err
	}

	quantityVO, err := valueobjects.NewQuantity(cmd.Quantity())
	if err != nil {
		return nil, err
	}

	currencyVO, err := valueobjects.NewCurrency(product.Currency)
	if err != nil {
		return nil, err
	}

	prefRequest := mercadopago.PreferenceRequest{
		Items: []mercadopago.PreferenceItem{
			{
				Title:      product.Title,
				Quantity:   cmd.Quantity(),
				UnitPrice:  product.Price,
				CurrencyID: product.Currency,
			},
		},
		Payer: mercadopago.PreferencePayer{
			Email: cmd.PayerEmail(),
		},
		BackURLs: mercadopago.PreferenceBackURLs{
			Success: successURL,
			Failure: failureURL,
			Pending: pendingURL,
		},
		AutoReturn:        "approved",
		ExternalReference: paymentUUID.String(),
	}

	if s.notificationURL != "" {
		prefRequest.NotificationURL = s.notificationURL
	}

	preference, err := s.mpClient.CreatePreference(ctx, prefRequest)
	if err != nil {
		return nil, err
	}

	preferenceIDVO, err := valueobjects.NewPreferenceID(preference.ID)
	if err != nil {
		return nil, err
	}

	productRefVO, err := valueobjects.NewProductReference(cmd.ProductID())
	if err != nil {
		return nil, err
	}

	statusVO, err := valueobjects.NewPaymentStatus(valueobjects.PaymentStatusPending)
	if err != nil {
		return nil, err
	}

	payment, err := entities.NewPaymentWithID(
		paymentUUID,
		productRefVO,
		product.Title,
		amountVO,
		currencyVO,
		quantityVO,
		preferenceIDVO,
		preference.InitPoint,
		statusVO,
	)
	if err != nil {
		return nil, err
	}

	if err := s.paymentRepo.Save(ctx, payment); err != nil {
		return nil, err
	}

	paymentID := valueobjects.NewPaymentIDFromUUID(paymentUUID)
	return &paymentID, nil
}

func (s *paymentCommandServiceImpl) HandleStatusUpdate(ctx context.Context, cmd commands.UpdatePaymentStatusCommand) error {
	paymentID, err := valueobjects.NewPaymentID(cmd.PaymentID())
	if err != nil {
		return err
	}

	payment, err := s.paymentRepo.FindByID(ctx, paymentID)
	if err != nil {
		return err
	}
	if payment == nil {
		return errors.New("payment not found")
	}

	normalized, err := normalizeStatus(cmd.Status())
	if err != nil {
		return err
	}

	statusVO, err := valueobjects.NewPaymentStatus(normalized)
	if err != nil {
		return err
	}

	payment.UpdateStatus(statusVO)
	return s.paymentRepo.Save(ctx, payment)
}

func normalizeStatus(status string) (string, error) {
	switch status {
	case valueobjects.PaymentStatusPending, "in_process":
		return valueobjects.PaymentStatusPending, nil
	case valueobjects.PaymentStatusPaid, "approved":
		return valueobjects.PaymentStatusPaid, nil
	case valueobjects.PaymentStatusFailed, "cancelled", "rejected", "refunded":
		return valueobjects.PaymentStatusFailed, nil
	default:
		return "", fmt.Errorf("unsupported payment status %s", status)
	}
}
