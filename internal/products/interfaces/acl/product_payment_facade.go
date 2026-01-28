package acl

import "context"

type ProductPaymentDetails struct {
	ProductID string
	Title     string
	Price     float64
	Currency  string
}

type ProductPaymentFacade interface {
	FetchProductForPayment(ctx context.Context, productID string) (*ProductPaymentDetails, error)
}
