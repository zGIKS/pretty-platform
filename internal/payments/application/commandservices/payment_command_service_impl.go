package commandservices

import (
	"context"
	"errors"
	"fmt"

	"go-service/internal/payments/domain/model/commands"
	"go-service/internal/payments/domain/model/entities"
	"go-service/internal/payments/domain/model/valueobjects"
	"go-service/internal/payments/domain/repositories"
	"go-service/internal/payments/domain/services"
	"go-service/internal/payments/infrastructure/mercadopago"
	"go-service/internal/products/interfaces/acl"
)

type paymentCommandServiceImpl struct {
	paymentRepo     repositories.PaymentRepository
	mpClient        *mercadopago.Client
	productFacade   acl.ProductPaymentFacade
	frontendBaseURL string
	notificationURL string
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
			Success: fmt.Sprintf("%s/payments/success", s.frontendBaseURL),
			Failure: fmt.Sprintf("%s/payments/failure", s.frontendBaseURL),
			Pending: fmt.Sprintf("%s/payments/pending", s.frontendBaseURL),
		},
		AutoReturn:        "approved",
		ExternalReference: product.ProductID,
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

	payment, err := entities.NewPayment(
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

	paymentID := payment.GetID()
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
