package controllers

import (
	"errors"
	"go-service/internal/payments/domain/model/commands"
	"go-service/internal/payments/domain/model/entities"
	"go-service/internal/payments/domain/model/queries"
	"go-service/internal/payments/domain/services"
	"go-service/internal/payments/infrastructure/mercadopago"
	"go-service/internal/payments/interfaces/rest/resources"
	"go-service/internal/products/interfaces/acl"
	"log"
	"strings"

	"github.com/gofiber/fiber/v2"
)

type PaymentController struct {
	commandService services.PaymentCommandService
	queryService   services.PaymentQueryService
	mpClient       *mercadopago.Client
}

func NewPaymentController(
	commandService services.PaymentCommandService,
	queryService services.PaymentQueryService,
	mpClient *mercadopago.Client,
) *PaymentController {
	return &PaymentController{
		commandService: commandService,
		queryService:   queryService,
		mpClient:       mpClient,
	}
}

// @Summary Create a payment preference
// @Description Creates a Mercado Pago checkout preference tied to a product
// @Tags payments
// @Accept json
// @Produce json
// @Param request body resources.CreatePaymentResource true "Payment creation request"
// @Success 201 {object} resources.PaymentCreationResponse
// @Failure 400 {object} resources.ErrorResponse
// @Failure 500 {object} resources.ErrorResponse
// @Router /payments [post]
func (c *PaymentController) CreatePayment(ctx *fiber.Ctx) error {
	var req resources.CreatePaymentResource
	if err := ctx.BodyParser(&req); err != nil {
		return ctx.Status(fiber.StatusBadRequest).JSON(resources.ErrorResponse{Error: "Invalid request data"})
	}

	cmd, err := commands.NewCreatePaymentCommand(req.ProductID, req.Quantity, req.PayerEmail)
	if err != nil {
		return ctx.Status(fiber.StatusBadRequest).JSON(resources.ErrorResponse{Error: "Invalid request data"})
	}

	paymentID, err := c.commandService.HandleCreate(ctx.Context(), cmd)
	if err != nil {
		var notFound acl.ProductNotFoundError
		if errors.As(err, &notFound) && notFound.NotFound() {
			return ctx.Status(fiber.StatusNotFound).JSON(resources.ErrorResponse{Error: "Product not found"})
		}

		log.Printf("CreatePayment failed: %v", err)
		if strings.Contains(strings.ToLower(err.Error()), "mercado pago") {
			if strings.Contains(err.Error(), "returned status 400") {
				return ctx.Status(fiber.StatusBadRequest).JSON(resources.ErrorResponse{Error: "Invalid Mercado Pago preference configuration (check FRONTEND_BASE_URL back_urls)"})
			}
			return ctx.Status(fiber.StatusBadGateway).JSON(resources.ErrorResponse{Error: "Failed to create Mercado Pago preference"})
		}
		return ctx.Status(fiber.StatusInternalServerError).JSON(resources.ErrorResponse{Error: "Internal server error"})
	}

	query, _ := queries.NewFindPaymentByIDQuery(paymentID.String())
	payment, err := c.queryService.HandleFindByID(ctx.Context(), query)
	if err != nil {
		return ctx.Status(fiber.StatusInternalServerError).JSON(resources.ErrorResponse{Error: "failed to read created payment"})
	}

	response := c.buildCreationResponse(payment)
	return ctx.Status(fiber.StatusCreated).JSON(response)
}

// @Summary Get payment by ID
// @Description Retrieve payment and preference details
// @Tags payments
// @Produce json
// @Param id path string true "Payment ID"
// @Success 200 {object} resources.PaymentResource
// @Failure 400 {object} resources.ErrorResponse
// @Failure 404 {object} resources.ErrorResponse
// @Router /payments/{id} [get]
func (c *PaymentController) GetPayment(ctx *fiber.Ctx) error {
	id := ctx.Params("id")
	query, err := queries.NewFindPaymentByIDQuery(id)
	if err != nil {
		return ctx.Status(fiber.StatusBadRequest).JSON(resources.ErrorResponse{Error: "Invalid request data"})
	}

	payment, err := c.queryService.HandleFindByID(ctx.Context(), query)
	if err != nil {
		return ctx.Status(fiber.StatusNotFound).JSON(resources.ErrorResponse{Error: "Payment not found"})
	}

	response := c.transformToResource(payment)
	return ctx.JSON(response)
}

type mpNotification struct {
	Type  string `json:"type"`
	Topic string `json:"topic"`
	ID    string `json:"id"`
	Data  struct {
		ID string `json:"id"`
	} `json:"data"`
}

// HandleNotifications receives Mercado Pago notifications (IPN/webhook) and updates payment status.
// Mercado Pago sends different payload/query formats depending on integration, so this tries the
// common ones: query `id`, `payment_id`, `data.id` or body `data.id`.
func (c *PaymentController) HandleNotifications(ctx *fiber.Ctx) error {
	var notif mpNotification
	_ = ctx.BodyParser(&notif)

	mpPaymentID := strings.TrimSpace(notif.Data.ID)
	if mpPaymentID == "" {
		mpPaymentID = strings.TrimSpace(notif.ID)
	}
	if mpPaymentID == "" {
		mpPaymentID = strings.TrimSpace(ctx.Query("data.id"))
	}
	if mpPaymentID == "" {
		mpPaymentID = strings.TrimSpace(ctx.Query("payment_id"))
	}
	if mpPaymentID == "" {
		mpPaymentID = strings.TrimSpace(ctx.Query("id"))
	}

	if mpPaymentID == "" {
		// Nothing to process; acknowledge to prevent retries storm.
		return ctx.SendStatus(fiber.StatusOK)
	}

	mpPayment, err := c.mpClient.GetPayment(ctx.Context(), mpPaymentID)
	if err != nil {
		// Acknowledge anyway; MP will retry, and transient errors happen.
		return ctx.SendStatus(fiber.StatusOK)
	}

	if strings.TrimSpace(mpPayment.ExternalReference) == "" {
		return ctx.SendStatus(fiber.StatusOK)
	}

	cmd, err := commands.NewUpdatePaymentStatusCommand(mpPayment.ExternalReference, mpPayment.Status)
	if err != nil {
		return ctx.SendStatus(fiber.StatusOK)
	}

	_ = c.commandService.HandleStatusUpdate(ctx.Context(), cmd)
	return ctx.SendStatus(fiber.StatusOK)
}

func (c *PaymentController) buildCreationResponse(payment *entities.Payment) resources.PaymentCreationResponse {
	return resources.PaymentCreationResponse{
		ID:            payment.GetID().String(),
		PreferenceID:  payment.GetPreferenceID().Value(),
		PreferenceURL: payment.GetPreferenceURL(),
		Status:        payment.GetStatus().Value(),
		PublicKey:     c.mpClient.PublicKey(),
		Amount:        payment.GetAmount().Value(),
		Currency:      payment.GetCurrency().Value(),
		Quantity:      payment.GetQuantity().Value(),
		ProductTitle:  payment.GetProductTitle(),
	}
}

func (c *PaymentController) transformToResource(payment *entities.Payment) resources.PaymentResource {
	return resources.PaymentResource{
		ID:            payment.GetID().String(),
		ProductID:     payment.GetProductReference().Value(),
		ProductTitle:  payment.GetProductTitle(),
		Amount:        payment.GetAmount().Value(),
		Currency:      payment.GetCurrency().Value(),
		Quantity:      payment.GetQuantity().Value(),
		PreferenceID:  payment.GetPreferenceID().Value(),
		PreferenceURL: payment.GetPreferenceURL(),
		Status:        payment.GetStatus().Value(),
		CreatedAt:     payment.GetCreatedAt(),
		UpdatedAt:     payment.GetUpdatedAt(),
	}
}
