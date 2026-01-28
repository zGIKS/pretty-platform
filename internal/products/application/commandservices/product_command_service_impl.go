package commandservices

import (
	"context"
	"errors"
	"go-service/internal/products/domain/model/commands"
	"go-service/internal/products/domain/model/entities"
	"go-service/internal/products/domain/model/valueobjects"
	"go-service/internal/products/domain/repositories"
	"go-service/internal/products/domain/services"
)

type productCommandServiceImpl struct {
	productRepo repositories.ProductRepository
}

func NewProductCommandService(productRepo repositories.ProductRepository) services.ProductCommandService {
	return &productCommandServiceImpl{
		productRepo: productRepo,
	}
}

func (s *productCommandServiceImpl) HandleCreate(ctx context.Context, cmd commands.CreateProductCommand) (*valueobjects.ProductID, error) {
	// Create value objects
	imageURL, err := valueobjects.NewImageURL(cmd.ImageURL())
	if err != nil {
		return nil, err
	}
	title, err := valueobjects.NewTitle(cmd.Title())
	if err != nil {
		return nil, err
	}
	price, err := valueobjects.NewPrice(cmd.Price())
	if err != nil {
		return nil, err
	}
	description, err := valueobjects.NewDescription(cmd.Description())
	if err != nil {
		return nil, err
	}
	category, err := valueobjects.NewCategory(cmd.Category())
	if err != nil {
		return nil, err
	}
	quantity, err := valueobjects.NewQuantity(cmd.Quantity())
	if err != nil {
		return nil, err
	}

	// Create entity
	product, err := entities.NewProduct(imageURL, title, price, description, category, quantity)
	if err != nil {
		return nil, err
	}

	// Persist
	if err := s.productRepo.Save(ctx, product); err != nil {
		return nil, err
	}

	productID := product.GetID()
	return &productID, nil
}

func (s *productCommandServiceImpl) HandleUpdate(ctx context.Context, cmd commands.UpdateProductCommand) error {
	// Find product
	productID, err := valueobjects.NewProductID(cmd.ProductID())
	if err != nil {
		return err
	}
	product, err := s.productRepo.FindByID(ctx, productID)
	if err != nil {
		return err
	}
	if product == nil {
		return errors.New("product not found")
	}

	// Update fields
	var imageURL *valueobjects.ImageURL
	if cmd.ImageURL() != nil {
		v, err := valueobjects.NewImageURL(*cmd.ImageURL())
		if err != nil {
			return err
		}
		imageURL = &v
	}
	var title *valueobjects.Title
	if cmd.Title() != nil {
		v, err := valueobjects.NewTitle(*cmd.Title())
		if err != nil {
			return err
		}
		title = &v
	}
	var price *valueobjects.Price
	if cmd.Price() != nil {
		v, err := valueobjects.NewPrice(*cmd.Price())
		if err != nil {
			return err
		}
		price = &v
	}
	var description *valueobjects.Description
	if cmd.Description() != nil {
		v, err := valueobjects.NewDescription(*cmd.Description())
		if err != nil {
			return err
		}
		description = &v
	}
	var category *valueobjects.Category
	if cmd.Category() != nil {
		v, err := valueobjects.NewCategory(*cmd.Category())
		if err != nil {
			return err
		}
		category = &v
	}
	var quantity *valueobjects.Quantity
	if cmd.Quantity() != nil {
		v, err := valueobjects.NewQuantity(*cmd.Quantity())
		if err != nil {
			return err
		}
		quantity = &v
	}

	product.Update(imageURL, title, price, description, category, quantity)

	// Save
	return s.productRepo.Save(ctx, product)
}

func (s *productCommandServiceImpl) HandleDelete(ctx context.Context, cmd commands.DeleteProductCommand) error {
	productID, err := valueobjects.NewProductID(cmd.ProductID())
	if err != nil {
		return err
	}
	return s.productRepo.Delete(ctx, productID)
}
