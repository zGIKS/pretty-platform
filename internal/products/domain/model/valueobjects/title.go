package valueobjects

import "errors"

type Title struct {
	value string `json:"value" gorm:"column:title"`
}

func NewTitle(value string) (Title, error) {
	if value == "" {
		return Title{}, errors.New("title cannot be empty")
	}
	if len(value) > 255 {
		return Title{}, errors.New("title too long")
	}
	return Title{value: value}, nil
}

func (t Title) Value() string {
	return t.value
}

func (t Title) String() string {
	return t.value
}
