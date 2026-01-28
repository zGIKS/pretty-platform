package queries

import "errors"

type FindPaymentByIDQuery struct {
	paymentID string
}

func NewFindPaymentByIDQuery(paymentID string) (FindPaymentByIDQuery, error) {
	if paymentID == "" {
		return FindPaymentByIDQuery{}, errors.New("payment ID cannot be empty")
	}
	return FindPaymentByIDQuery{paymentID: paymentID}, nil
}

func (q FindPaymentByIDQuery) PaymentID() string { return q.paymentID }
