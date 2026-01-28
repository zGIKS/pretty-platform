package valueobjects

import "errors"

type Description struct {
	value string `gorm:"column:description"`
}

func NewDescription(value string) (Description, error) {
	if len(value) > 1000 {
		return Description{}, errors.New("description too long")
	}
	return Description{value: value}, nil
}

func (d Description) Value() string {
	return d.value
}

func (d Description) String() string {
	return d.value
}
