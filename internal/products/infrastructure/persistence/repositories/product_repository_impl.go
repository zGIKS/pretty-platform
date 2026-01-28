package repositories

import (
	"context"
	"go-service/internal/products/domain/model/entities"
	"go-service/internal/products/domain/model/valueobjects"
	"go-service/internal/products/domain/repositories"

	"gorm.io/gorm"
)

type productRepositoryImpl struct {
	db *gorm.DB
}

func NewProductRepository(db *gorm.DB) repositories.ProductRepository {
	return &productRepositoryImpl{db: db}
}

func (r *productRepositoryImpl) Save(ctx context.Context, product *entities.Product) error {
	return r.db.WithContext(ctx).Save(product).Error
}

func (r *productRepositoryImpl) FindByID(ctx context.Context, id valueobjects.ProductID) (*entities.Product, error) {
	var product entities.Product
	err := r.db.WithContext(ctx).Where("id = ?", id.Value()).First(&product).Error
	if err != nil {
		return nil, err
	}
	return &product, nil
}

func (r *productRepositoryImpl) FindAll(ctx context.Context, limit, offset *int) ([]*entities.Product, error) {
	var products []*entities.Product
	query := r.db.WithContext(ctx)
	if limit != nil {
		query = query.Limit(*limit)
	}
	if offset != nil {
		query = query.Offset(*offset)
	}
	err := query.Find(&products).Error
	return products, err
}

func (r *productRepositoryImpl) FindByCategory(ctx context.Context, category string, limit, offset *int) ([]*entities.Product, error) {
	var products []*entities.Product
	query := r.db.WithContext(ctx).Where("category = ?", category)
	if limit != nil {
		query = query.Limit(*limit)
	}
	if offset != nil {
		query = query.Offset(*offset)
	}
	err := query.Find(&products).Error
	return products, err
}

func (r *productRepositoryImpl) Delete(ctx context.Context, id valueobjects.ProductID) error {
	return r.db.WithContext(ctx).Where("id = ?", id.Value()).Delete(&entities.Product{}).Error
}
