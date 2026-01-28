package controllers

import (
	"strconv"

	"go-service/internal/products/domain/model/commands"
	"go-service/internal/products/domain/model/entities"
	"go-service/internal/products/domain/model/queries"
	"go-service/internal/products/domain/services"
	"go-service/internal/products/interfaces/rest/resources"

	"github.com/gofiber/fiber/v2"
)

type ProductController struct {
	commandService services.ProductCommandService
	queryService   services.ProductQueryService
}

func NewProductController(
	commandService services.ProductCommandService,
	queryService services.ProductQueryService,
) *ProductController {
	return &ProductController{
		commandService: commandService,
		queryService:   queryService,
	}
}

// @Summary Create a new product
// @Description Create a new product with the provided details
// @Tags products
// @Accept json
// @Produce json
// @Param request body resources.CreateProductResource true "Product creation request"
// @Success 201 {object} resources.ProductResource
// @Failure 400 {object} resources.ErrorResponse
// @Failure 500 {object} resources.ErrorResponse
// @Router /products [post]
func (c *ProductController) CreateProduct(ctx *fiber.Ctx) error {
	var req resources.CreateProductResource
	if err := ctx.BodyParser(&req); err != nil {
		return ctx.Status(fiber.StatusBadRequest).JSON(resources.ErrorResponse{Error: err.Error()})
	}

	// Transform resource to command
	cmd, err := commands.NewCreateProductCommand(
		req.ImageURL, req.Title, req.Price, req.Description, req.Category, req.Quantity,
	)
	if err != nil {
		return ctx.Status(fiber.StatusBadRequest).JSON(resources.ErrorResponse{Error: err.Error()})
	}

	// Execute command
	productID, err := c.commandService.HandleCreate(ctx.Context(), cmd)
	if err != nil {
		return ctx.Status(fiber.StatusInternalServerError).JSON(resources.ErrorResponse{Error: err.Error()})
	}

	// Retrieve created product
	findQuery, _ := queries.NewFindProductByIDQuery(productID.String())
	product, err := c.queryService.HandleFindByID(ctx.Context(), findQuery)
	if err != nil {
		return ctx.Status(fiber.StatusInternalServerError).JSON(resources.ErrorResponse{Error: "Failed to retrieve created product"})
	}

	response := c.transformProductToResource(product)
	return ctx.Status(fiber.StatusCreated).JSON(response)
}

// @Summary Get product by ID
// @Description Retrieve a product by its ID
// @Tags products
// @Produce json
// @Param id path string true "Product ID"
// @Success 200 {object} resources.ProductResource
// @Failure 400 {object} resources.ErrorResponse
// @Failure 404 {object} resources.ErrorResponse
// @Router /products/{id} [get]
func (c *ProductController) GetProduct(ctx *fiber.Ctx) error {
	id := ctx.Params("id")
	query, err := queries.NewFindProductByIDQuery(id)
	if err != nil {
		return ctx.Status(fiber.StatusBadRequest).JSON(resources.ErrorResponse{Error: err.Error()})
	}

	product, err := c.queryService.HandleFindByID(ctx.Context(), query)
	if err != nil {
		return ctx.Status(fiber.StatusNotFound).JSON(resources.ErrorResponse{Error: "Product not found"})
	}

	response := c.transformProductToResource(product)
	return ctx.JSON(response)
}

// @Summary Get all products
// @Description Retrieve all products with optional pagination and category filter
// @Tags products
// @Produce json
// @Param limit query int false "Limit"
// @Param offset query int false "Offset"
// @Param category query string false "Category filter (URL encoded for spaces, e.g., 'Cuidado%20de%20la%20piel')"
// @Success 200 {array} resources.ProductResource
// @Router /products [get]
func (c *ProductController) GetAllProducts(ctx *fiber.Ctx) error {
	categoryParam := ctx.Query("category")

	// Parse pagination parameters
	var limit, offset *int
	if limitStr := ctx.Query("limit"); limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 {
			limit = &l
		}
	}
	if offsetStr := ctx.Query("offset"); offsetStr != "" {
		if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
			offset = &o
		}
	}

	var products []*entities.Product
	var err error

	if categoryParam != "" {
		// Filter by category
		query, _ := queries.NewFindProductsByCategoryQuery(categoryParam)
		if limit != nil {
			if offset != nil {
				query = query.WithPagination(*limit, *offset)
			} else {
				query = query.WithPagination(*limit, 0)
			}
		}
		products, err = c.queryService.HandleFindByCategory(ctx.Context(), query)
	} else {
		// Get all products
		query := queries.NewGetAllProductsQuery()
		if limit != nil {
			if offset != nil {
				query = query.WithPagination(*limit, *offset)
			} else {
				query = query.WithPagination(*limit, 0)
			}
		}
		products, err = c.queryService.HandleGetAll(ctx.Context(), query)
	}

	if err != nil {
		return ctx.Status(fiber.StatusInternalServerError).JSON(resources.ErrorResponse{Error: err.Error()})
	}

	var responses []resources.ProductResource
	for _, product := range products {
		responses = append(responses, c.transformProductToResource(product))
	}
	return ctx.JSON(responses)
}

// @Summary Update product
// @Description Update an existing product
// @Tags products
// @Accept json
// @Produce json
// @Param id path string true "Product ID"
// @Param request body resources.UpdateProductResource true "Product update request"
// @Success 200 {object} resources.ProductResource
// @Failure 400 {object} resources.ErrorResponse
// @Failure 404 {object} resources.ErrorResponse
// @Router /products/{id} [put]
func (c *ProductController) UpdateProduct(ctx *fiber.Ctx) error {
	id := ctx.Params("id")
	var req resources.UpdateProductResource
	if err := ctx.BodyParser(&req); err != nil {
		return ctx.Status(fiber.StatusBadRequest).JSON(resources.ErrorResponse{Error: err.Error()})
	}

	cmd, err := commands.NewUpdateProductCommand(id, req.ImageURL, req.Title, req.Price, req.Description, req.Category, req.Quantity)
	if err != nil {
		return ctx.Status(fiber.StatusBadRequest).JSON(resources.ErrorResponse{Error: err.Error()})
	}

	err = c.commandService.HandleUpdate(ctx.Context(), cmd)
	if err != nil {
		return ctx.Status(fiber.StatusInternalServerError).JSON(resources.ErrorResponse{Error: err.Error()})
	}

	// Retrieve updated product
	findQuery, _ := queries.NewFindProductByIDQuery(id)
	product, err := c.queryService.HandleFindByID(ctx.Context(), findQuery)
	if err != nil {
		return ctx.Status(fiber.StatusInternalServerError).JSON(resources.ErrorResponse{Error: "Failed to retrieve updated product"})
	}

	response := c.transformProductToResource(product)
	return ctx.JSON(response)
}

// @Summary Delete product
// @Description Delete a product by ID
// @Tags products
// @Param id path string true "Product ID"
// @Success 204
// @Failure 400 {object} resources.ErrorResponse
// @Failure 500 {object} resources.ErrorResponse
// @Router /products/{id} [delete]
func (c *ProductController) DeleteProduct(ctx *fiber.Ctx) error {
	id := ctx.Params("id")
	cmd, err := commands.NewDeleteProductCommand(id)
	if err != nil {
		return ctx.Status(fiber.StatusBadRequest).JSON(resources.ErrorResponse{Error: err.Error()})
	}

	err = c.commandService.HandleDelete(ctx.Context(), cmd)
	if err != nil {
		return ctx.Status(fiber.StatusInternalServerError).JSON(resources.ErrorResponse{Error: err.Error()})
	}

	return ctx.SendStatus(fiber.StatusNoContent)
}

func (c *ProductController) transformProductToResource(product *entities.Product) resources.ProductResource {
	return resources.ProductResource{
		ID:          product.GetID().String(),
		ImageURL:    product.GetImageURL().Value(),
		Title:       product.GetTitle().Value(),
		Price:       product.GetPrice().Value(),
		Description: product.GetDescription().Value(),
		Category:    product.GetCategory().Value(),
		Quantity:    product.GetQuantity().Value(),
		CreatedAt:   product.GetCreatedAt(),
		UpdatedAt:   product.GetUpdatedAt(),
	}
}
