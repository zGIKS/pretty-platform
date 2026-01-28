package repositories

import (
	"context"
	"go-service/internal/products/domain/model/entities"
	"go-service/internal/products/domain/model/valueobjects"
)

type ProductRepository interface {
	Save(ctx context.Context, product *entities.Product) error
	FindByID(ctx context.Context, id valueobjects.ProductID) (*entities.Product, error)
	FindAll(ctx context.Context, limit, offset *int) ([]*entities.Product, error)
	FindByCategory(ctx context.Context, category string, limit, offset *int) ([]*entities.Product, error)
	Delete(ctx context.Context, id valueobjects.ProductID) error
}
