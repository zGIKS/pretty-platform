package valueobjects

import "errors"

type Category struct {
	value string `gorm:"column:category"`
}

func NewCategory(value string) (Category, error) {
	if value == "" {
		return Category{}, errors.New("category cannot be empty")
	}
	return Category{value: value}, nil
}

func (c Category) Value() string {
	return c.value
}

func (c Category) String() string {
	return c.value
}
