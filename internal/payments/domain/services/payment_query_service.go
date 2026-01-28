package services

import (
	"context"

	"go-service/internal/payments/domain/model/entities"
	"go-service/internal/payments/domain/model/queries"
)

type PaymentQueryService interface {
	HandleFindByID(ctx context.Context, query queries.FindPaymentByIDQuery) (*entities.Payment, error)
}
