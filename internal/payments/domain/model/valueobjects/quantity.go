package valueobjects

import "errors"

type Quantity struct {
	value int `gorm:"column:quantity"`
}

func NewQuantity(value int) (Quantity, error) {
	if value <= 0 {
		return Quantity{}, errors.New("quantity must be greater than zero")
	}
	return Quantity{value: value}, nil
}

func (q Quantity) Value() int {
	return q.value
}
