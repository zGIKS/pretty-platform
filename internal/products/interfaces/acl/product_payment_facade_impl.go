package acl

import (
	"context"
	"fmt"

	"go-service/internal/products/domain/model/queries"
	"go-service/internal/products/domain/services"
)

type productPaymentFacadeImpl struct {
	queryService    services.ProductQueryService
	defaultCurrency string
}

func NewProductPaymentFacade(queryService services.ProductQueryService, defaultCurrency string) ProductPaymentFacade {
	return &productPaymentFacadeImpl{
		queryService:    queryService,
		defaultCurrency: defaultCurrency,
	}
}

func (f *productPaymentFacadeImpl) FetchProductForPayment(ctx context.Context, productID string) (*ProductPaymentDetails, error) {
	query, err := queries.NewFindProductByIDQuery(productID)
	if err != nil {
		return nil, err
	}

	product, err := f.queryService.HandleFindByID(ctx, query)
	if err != nil {
		return nil, err
	}
	if product == nil {
		return nil, fmt.Errorf("product %s not found", productID)
	}

	return &ProductPaymentDetails{
		ProductID: product.GetID().String(),
		Title:     product.GetTitle().Value(),
		Price:     product.GetPrice().Value(),
		Currency:  f.defaultCurrency,
	}, nil
}
