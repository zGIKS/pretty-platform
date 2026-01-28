package valueobjects

import (
	"errors"
	"fmt"
)

type Amount struct {
	value float64 `gorm:"column:amount"`
}

func NewAmount(value float64) (Amount, error) {
	if value < 0 {
		return Amount{}, errors.New("amount cannot be negative")
	}
	return Amount{value: value}, nil
}

func (a Amount) Value() float64 {
	return a.value
}

func (a Amount) String() string {
	return fmt.Sprintf("%.2f", a.value)
}
