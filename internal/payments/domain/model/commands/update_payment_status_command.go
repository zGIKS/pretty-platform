package commands

import "errors"

type UpdatePaymentStatusCommand struct {
	paymentID string
	status    string
}

func NewUpdatePaymentStatusCommand(paymentID, status string) (UpdatePaymentStatusCommand, error) {
	if paymentID == "" {
		return UpdatePaymentStatusCommand{}, errors.New("payment ID cannot be empty")
	}
	if status == "" {
		return UpdatePaymentStatusCommand{}, errors.New("status cannot be empty")
	}
	return UpdatePaymentStatusCommand{paymentID: paymentID, status: status}, nil
}

func (c UpdatePaymentStatusCommand) PaymentID() string { return c.paymentID }
func (c UpdatePaymentStatusCommand) Status() string    { return c.status }
