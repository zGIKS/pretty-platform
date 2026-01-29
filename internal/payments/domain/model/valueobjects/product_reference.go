package valueobjects

import "errors"

type ProductReference struct {
	value string `gorm:"column:product_id"`
}

func NewProductReference(value string) (ProductReference, error) {
	if value == "" {
		return ProductReference{}, errors.New("product reference cannot be empty")
	}
	return ProductReference{value: value}, nil
}

func (p ProductReference) Value() string {
	return p.value
}
