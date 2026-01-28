package valueobjects

import "errors"

type PreferenceID struct {
	value string `gorm:"column:preference_id"`
}

func NewPreferenceID(value string) (PreferenceID, error) {
	if value == "" {
		return PreferenceID{}, errors.New("preference ID cannot be empty")
	}
	return PreferenceID{value: value}, nil
}

func (p PreferenceID) Value() string {
	return p.value
}
