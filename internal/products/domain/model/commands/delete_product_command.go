package commands

import (
	"errors"
)

type DeleteProductCommand struct {
	productID string
}

func NewDeleteProductCommand(productID string) (DeleteProductCommand, error) {
	if productID == "" {
		return DeleteProductCommand{}, errors.New("product ID cannot be empty")
	}
	return DeleteProductCommand{productID: productID}, nil
}

func (c DeleteProductCommand) ProductID() string { return c.productID }
