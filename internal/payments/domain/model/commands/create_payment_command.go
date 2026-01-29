package commands

import "errors"

type CreatePaymentCommand struct {
	productID  string
	quantity   int
	payerEmail string
}

func NewCreatePaymentCommand(productID string, quantity int, payerEmail string) (CreatePaymentCommand, error) {
	if productID == "" {
		return CreatePaymentCommand{}, errors.New("product ID cannot be empty")
	}
	if quantity <= 0 {
		return CreatePaymentCommand{}, errors.New("quantity must be greater than zero")
	}
	if payerEmail == "" {
		return CreatePaymentCommand{}, errors.New("payer email cannot be empty")
	}
	return CreatePaymentCommand{productID: productID, quantity: quantity, payerEmail: payerEmail}, nil
}

func (c CreatePaymentCommand) ProductID() string  { return c.productID }
func (c CreatePaymentCommand) Quantity() int      { return c.quantity }
func (c CreatePaymentCommand) PayerEmail() string { return c.payerEmail }
