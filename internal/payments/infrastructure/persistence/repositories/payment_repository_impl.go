package repositories

import (
	"context"

	"go-service/internal/payments/domain/model/entities"
	"go-service/internal/payments/domain/model/valueobjects"
	"go-service/internal/payments/domain/repositories"

	"gorm.io/gorm"
)

type paymentRepositoryImpl struct {
	db *gorm.DB
}

func NewPaymentRepository(db *gorm.DB) repositories.PaymentRepository {
	return &paymentRepositoryImpl{db: db}
}

func (r *paymentRepositoryImpl) Save(ctx context.Context, payment *entities.Payment) error {
	return r.db.WithContext(ctx).Save(payment).Error
}

func (r *paymentRepositoryImpl) FindByID(ctx context.Context, id valueobjects.PaymentID) (*entities.Payment, error) {
	var payment entities.Payment
	err := r.db.WithContext(ctx).Where("id = ?", id.Value()).First(&payment).Error
	if err != nil {
		return nil, err
	}
	return &payment, nil
}
