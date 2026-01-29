package valueobjects

import (
	"errors"
	"net/url"
)

type ImageURL struct {
	value string `gorm:"column:image_url"`
}

func NewImageURL(value string) (ImageURL, error) {
	if value == "" {
		return ImageURL{}, errors.New("image URL cannot be empty")
	}
	if _, err := url.Parse(value); err != nil {
		return ImageURL{}, errors.New("invalid URL format")
	}
	return ImageURL{value: value}, nil
}

func (i ImageURL) Value() string {
	return i.value
}

func (i ImageURL) String() string {
	return i.value
}
