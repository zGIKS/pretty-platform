package services

import (
	"context"
	"go-service/internal/products/domain/model/commands"
	"go-service/internal/products/domain/model/valueobjects"
)

type ProductCommandService interface {
	HandleCreate(ctx context.Context, cmd commands.CreateProductCommand) (*valueobjects.ProductID, error)
	HandleUpdate(ctx context.Context, cmd commands.UpdateProductCommand) error
	HandleDelete(ctx context.Context, cmd commands.DeleteProductCommand) error
}
