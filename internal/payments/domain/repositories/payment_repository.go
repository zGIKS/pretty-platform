package repositories

import (
	"context"

	"go-service/internal/payments/domain/model/entities"
	"go-service/internal/payments/domain/model/valueobjects"
)

type PaymentRepository interface {
	Save(ctx context.Context, payment *entities.Payment) error
	FindByID(ctx context.Context, id valueobjects.PaymentID) (*entities.Payment, error)
}
