package valueobjects

import (
	"errors"
	"fmt"
)

type Price struct {
	value float64 `gorm:"column:price"`
}

func NewPrice(value float64) (Price, error) {
	if value < 0 {
		return Price{}, errors.New("price cannot be negative")
	}
	return Price{value: value}, nil
}

func (p Price) Value() float64 {
	return p.value
}

func (p Price) String() string {
	return fmt.Sprintf("%.2f", p.value)
}
