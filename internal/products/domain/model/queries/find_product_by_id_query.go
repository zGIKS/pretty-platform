package queries

import "errors"

type FindProductByIDQuery struct {
	productID string
}

func NewFindProductByIDQuery(productID string) (FindProductByIDQuery, error) {
	if productID == "" {
		return FindProductByIDQuery{}, errors.New("product ID cannot be empty")
	}
	return FindProductByIDQuery{productID: productID}, nil
}

func (q FindProductByIDQuery) ProductID() string { return q.productID }
