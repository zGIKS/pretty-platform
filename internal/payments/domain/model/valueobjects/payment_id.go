package valueobjects

import (
	"errors"

	"github.com/google/uuid"
)

type PaymentID struct {
	value uuid.UUID `gorm:"type:uuid;column:payment_id"`
}

func NewPaymentID(value string) (PaymentID, error) {
	if value == "" {
		return PaymentID{}, errors.New("payment ID cannot be empty")
	}
	parsed, err := uuid.Parse(value)
	if err != nil {
		return PaymentID{}, errors.New("invalid UUID format for payment ID")
	}
	return PaymentID{value: parsed}, nil
}

func NewPaymentIDFromUUID(value uuid.UUID) PaymentID {
	return PaymentID{value: value}
}

func (p PaymentID) Value() uuid.UUID {
	return p.value
}

func (p PaymentID) String() string {
	return p.value.String()
}
