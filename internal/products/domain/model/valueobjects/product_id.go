package valueobjects

import (
	"errors"

	"github.com/google/uuid"
)

type ProductID struct {
	value uuid.UUID `gorm:"type:uuid;column:product_id"`
}

func NewProductID(value string) (ProductID, error) {
	if value == "" {
		return ProductID{}, errors.New("product ID cannot be empty")
	}
	parsed, err := uuid.Parse(value)
	if err != nil {
		return ProductID{}, errors.New("invalid UUID format")
	}
	return ProductID{value: parsed}, nil
}

func NewProductIDFromUUID(value uuid.UUID) ProductID {
	return ProductID{value: value}
}

func (p ProductID) Value() uuid.UUID {
	return p.value
}

func (p ProductID) String() string {
	return p.value.String()
}
