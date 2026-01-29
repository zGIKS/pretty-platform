package entities

import (
	"errors"
	"time"

	"go-service/internal/payments/domain/model/valueobjects"

	"github.com/google/uuid"
)

type Payment struct {
	ID            uuid.UUID `gorm:"type:uuid;primaryKey;column:id"`
	ProductID     string    `gorm:"column:product_id"`
	ProductTitle  string    `gorm:"column:product_title"`
	Amount        float64   `gorm:"column:amount"`
	Currency      string    `gorm:"column:currency"`
	Quantity      int       `gorm:"column:quantity"`
	PreferenceID  string    `gorm:"column:preference_id"`
	PreferenceURL string    `gorm:"column:preference_url"`
	Status        string    `gorm:"column:status"`
	CreatedAt     time.Time `gorm:"column:created_at"`
	UpdatedAt     time.Time `gorm:"column:updated_at"`
}

func NewPayment(
	productRef valueobjects.ProductReference,
	productTitle string,
	amount valueobjects.Amount,
	currency valueobjects.Currency,
	quantity valueobjects.Quantity,
	preferenceID valueobjects.PreferenceID,
	preferenceURL string,
	status valueobjects.PaymentStatus,
) (*Payment, error) {
	now := time.Now()
	id := uuid.New()
	return &Payment{
		ID:            id,
		ProductID:     productRef.Value(),
		ProductTitle:  productTitle,
		Amount:        amount.Value(),
		Currency:      currency.Value(),
		Quantity:      quantity.Value(),
		PreferenceID:  preferenceID.Value(),
		PreferenceURL: preferenceURL,
		Status:        status.Value(),
		CreatedAt:     now,
		UpdatedAt:     now,
	}, nil
}

func NewPaymentWithID(
	id uuid.UUID,
	productRef valueobjects.ProductReference,
	productTitle string,
	amount valueobjects.Amount,
	currency valueobjects.Currency,
	quantity valueobjects.Quantity,
	preferenceID valueobjects.PreferenceID,
	preferenceURL string,
	status valueobjects.PaymentStatus,
) (*Payment, error) {
	if id == uuid.Nil {
		return nil, errors.New("payment ID cannot be nil")
	}

	now := time.Now()
	return &Payment{
		ID:            id,
		ProductID:     productRef.Value(),
		ProductTitle:  productTitle,
		Amount:        amount.Value(),
		Currency:      currency.Value(),
		Quantity:      quantity.Value(),
		PreferenceID:  preferenceID.Value(),
		PreferenceURL: preferenceURL,
		Status:        status.Value(),
		CreatedAt:     now,
		UpdatedAt:     now,
	}, nil
}

func (p *Payment) GetID() valueobjects.PaymentID {
	return valueobjects.NewPaymentIDFromUUID(p.ID)
}

func (p *Payment) GetProductReference() valueobjects.ProductReference {
	v, _ := valueobjects.NewProductReference(p.ProductID)
	return v
}

func (p *Payment) GetAmount() valueobjects.Amount {
	v, _ := valueobjects.NewAmount(p.Amount)
	return v
}

func (p *Payment) GetCurrency() valueobjects.Currency {
	v, _ := valueobjects.NewCurrency(p.Currency)
	return v
}

func (p *Payment) GetQuantity() valueobjects.Quantity {
	v, _ := valueobjects.NewQuantity(p.Quantity)
	return v
}

func (p *Payment) GetPreferenceID() valueobjects.PreferenceID {
	v, _ := valueobjects.NewPreferenceID(p.PreferenceID)
	return v
}

func (p *Payment) GetStatus() valueobjects.PaymentStatus {
	v, _ := valueobjects.NewPaymentStatus(p.Status)
	return v
}

func (p *Payment) GetPreferenceURL() string {
	return p.PreferenceURL
}

func (p *Payment) GetProductTitle() string {
	return p.ProductTitle
}

func (p *Payment) GetCreatedAt() time.Time {
	return p.CreatedAt
}

func (p *Payment) GetUpdatedAt() time.Time {
	return p.UpdatedAt
}

func (p *Payment) UpdateStatus(status valueobjects.PaymentStatus) {
	p.Status = status.Value()
	p.UpdatedAt = time.Now()
}
