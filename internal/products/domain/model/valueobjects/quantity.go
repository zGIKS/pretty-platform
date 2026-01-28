package valueobjects

import (
	"errors"
	"fmt"
)

type Quantity struct {
	value int `gorm:"column:quantity"`
}

func NewQuantity(value int) (Quantity, error) {
	if value < 0 {
		return Quantity{}, errors.New("quantity cannot be negative")
	}
	return Quantity{value: value}, nil
}

func (q Quantity) Value() int {
	return q.value
}

func (q Quantity) String() string {
	return fmt.Sprintf("%d", q.value)
}
