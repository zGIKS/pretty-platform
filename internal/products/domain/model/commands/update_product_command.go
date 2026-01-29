package commands

import (
	"errors"
)

type UpdateProductCommand struct {
	productID   string
	imageURL    *string
	title       *string
	price       *float64
	description *string
	category    *string
	quantity    *int
}

func NewUpdateProductCommand(
	productID string,
	imageURL, title *string,
	price *float64,
	description, category *string,
	quantity *int,
) (UpdateProductCommand, error) {
	if productID == "" {
		return UpdateProductCommand{}, errors.New("product ID cannot be empty")
	}
	return UpdateProductCommand{
		productID:   productID,
		imageURL:    imageURL,
		title:       title,
		price:       price,
		description: description,
		category:    category,
		quantity:    quantity,
	}, nil
}

func (c UpdateProductCommand) ProductID() string    { return c.productID }
func (c UpdateProductCommand) ImageURL() *string    { return c.imageURL }
func (c UpdateProductCommand) Title() *string       { return c.title }
func (c UpdateProductCommand) Price() *float64      { return c.price }
func (c UpdateProductCommand) Description() *string { return c.description }
func (c UpdateProductCommand) Category() *string    { return c.category }
func (c UpdateProductCommand) Quantity() *int       { return c.quantity }
