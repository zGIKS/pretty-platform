package services

import (
	"context"
	"go-service/internal/products/domain/model/entities"
	"go-service/internal/products/domain/model/queries"
)

type ProductQueryService interface {
	HandleFindByID(ctx context.Context, query queries.FindProductByIDQuery) (*entities.Product, error)
	HandleGetAll(ctx context.Context, query queries.GetAllProductsQuery) ([]*entities.Product, error)
	HandleFindByCategory(ctx context.Context, query queries.FindProductsByCategoryQuery) ([]*entities.Product, error)
}
