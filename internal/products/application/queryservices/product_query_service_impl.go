package queryservices

import (
	"context"
	"go-service/internal/products/domain/model/entities"
	"go-service/internal/products/domain/model/queries"
	"go-service/internal/products/domain/model/valueobjects"
	"go-service/internal/products/domain/repositories"
	"go-service/internal/products/domain/services"
)

type productQueryServiceImpl struct {
	productRepo repositories.ProductRepository
}

func NewProductQueryService(productRepo repositories.ProductRepository) services.ProductQueryService {
	return &productQueryServiceImpl{
		productRepo: productRepo,
	}
}

func (s *productQueryServiceImpl) HandleFindByID(ctx context.Context, query queries.FindProductByIDQuery) (*entities.Product, error) {
	productID, err := valueobjects.NewProductID(query.ProductID())
	if err != nil {
		return nil, err
	}
	return s.productRepo.FindByID(ctx, productID)
}

func (s *productQueryServiceImpl) HandleGetAll(ctx context.Context, query queries.GetAllProductsQuery) ([]*entities.Product, error) {
	return s.productRepo.FindAll(ctx, query.Limit(), query.Offset())
}

func (s *productQueryServiceImpl) HandleFindByCategory(ctx context.Context, query queries.FindProductsByCategoryQuery) ([]*entities.Product, error) {
	return s.productRepo.FindByCategory(ctx, query.Category(), query.Limit(), query.Offset())
}
