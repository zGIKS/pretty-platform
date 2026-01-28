package services

import (
	"context"

	"go-service/internal/payments/domain/model/commands"
	"go-service/internal/payments/domain/model/valueobjects"
)

type PaymentCommandService interface {
	HandleCreate(ctx context.Context, cmd commands.CreatePaymentCommand) (*valueobjects.PaymentID, error)
	HandleStatusUpdate(ctx context.Context, cmd commands.UpdatePaymentStatusCommand) error
}
