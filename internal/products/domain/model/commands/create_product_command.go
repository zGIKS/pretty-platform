package commands

import (
	"errors"
)

type CreateProductCommand struct {
	imageURL    string
	title       string
	price       float64
	description string
	category    string
	quantity    int
}

func NewCreateProductCommand(
	imageURL, title string,
	price float64,
	description, category string,
	quantity int,
) (CreateProductCommand, error) {
	if imageURL == "" {
		return CreateProductCommand{}, errors.New("image URL cannot be empty")
	}
	if title == "" {
		return CreateProductCommand{}, errors.New("title cannot be empty")
	}
	if price < 0 {
		return CreateProductCommand{}, errors.New("price cannot be negative")
	}
	if category == "" {
		return CreateProductCommand{}, errors.New("category cannot be empty")
	}
	if quantity < 0 {
		return CreateProductCommand{}, errors.New("quantity cannot be negative")
	}
	return CreateProductCommand{
		imageURL:    imageURL,
		title:       title,
		price:       price,
		description: description,
		category:    category,
		quantity:    quantity,
	}, nil
}

func (c CreateProductCommand) ImageURL() string    { return c.imageURL }
func (c CreateProductCommand) Title() string       { return c.title }
func (c CreateProductCommand) Price() float64      { return c.price }
func (c CreateProductCommand) Description() string { return c.description }
func (c CreateProductCommand) Category() string    { return c.category }
func (c CreateProductCommand) Quantity() int       { return c.quantity }
