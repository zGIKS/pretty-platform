package valueobjects

import "errors"

type Currency struct {
	value string `gorm:"column:currency"`
}

func NewCurrency(value string) (Currency, error) {
	if value == "" {
		return Currency{}, errors.New("currency cannot be empty")
	}
	if len(value) != 3 {
		return Currency{}, errors.New("currency must be a 3-letter ISO code")
	}
	return Currency{value: value}, nil
}

func (c Currency) Value() string {
	return c.value
}

func (c Currency) String() string {
	return c.value
}
