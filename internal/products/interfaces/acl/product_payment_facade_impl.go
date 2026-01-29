package acl

import (
	"context"
	"errors"
	"fmt"

	"go-service/internal/products/domain/model/queries"
	"go-service/internal/products/domain/services"

	"gorm.io/gorm"
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

type productNotFoundError struct {
	productID string
}

func (e productNotFoundError) Error() string {
	return fmt.Sprintf("product %s not found", e.productID)
}

func (e productNotFoundError) NotFound() bool { return true }

func (f *productPaymentFacadeImpl) FetchProductForPayment(ctx context.Context, productID string) (*ProductPaymentDetails, error) {
	query, err := queries.NewFindProductByIDQuery(productID)
	if err != nil {
		return nil, err
	}

	product, err := f.queryService.HandleFindByID(ctx, query)
	if err != nil {
		if errors.Is(err, gorm.ErrRecordNotFound) {
			return nil, productNotFoundError{productID: productID}
		}
		return nil, err
	}
	if product == nil {
		return nil, productNotFoundError{productID: productID}
	}

	return &ProductPaymentDetails{
		ProductID: product.GetID().String(),
		Title:     product.GetTitle().Value(),
		Price:     product.GetPrice().Value(),
		Currency:  f.defaultCurrency,
	}, nil
}
