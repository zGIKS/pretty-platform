package queryservices

import (
	"context"

	"go-service/internal/payments/domain/model/entities"
	"go-service/internal/payments/domain/model/queries"
	"go-service/internal/payments/domain/model/valueobjects"
	"go-service/internal/payments/domain/repositories"
	"go-service/internal/payments/domain/services"
)

type paymentQueryServiceImpl struct {
	paymentRepo repositories.PaymentRepository
}

func NewPaymentQueryService(paymentRepo repositories.PaymentRepository) services.PaymentQueryService {
	return &paymentQueryServiceImpl{paymentRepo: paymentRepo}
}

func (s *paymentQueryServiceImpl) HandleFindByID(ctx context.Context, query queries.FindPaymentByIDQuery) (*entities.Payment, error) {
	paymentID, err := valueobjects.NewPaymentID(query.PaymentID())
	if err != nil {
		return nil, err
	}
	return s.paymentRepo.FindByID(ctx, paymentID)
}
