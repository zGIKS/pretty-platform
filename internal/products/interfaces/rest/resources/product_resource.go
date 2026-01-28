package resources

import "time"

type ProductResource struct {
	ID          string    `json:"id" example:"550e8400-e29b-41d4-a716-446655440000"`
	ImageURL    string    `json:"image_url" example:"https://example.com/image.jpg" validate:"required,url"`
	Title       string    `json:"title" example:"Product Title" validate:"required,min=1,max=255"`
	Price       float64   `json:"price" example:"99.99" validate:"required,min=0"`
	Description string    `json:"description" example:"Product description" validate:"max=1000"`
	Category    string    `json:"category" example:"Electronics" validate:"required"`
	Quantity    int       `json:"quantity" example:"10" validate:"required,min=0"`
	CreatedAt   time.Time `json:"created_at" example:"2023-01-01T00:00:00Z"`
	UpdatedAt   time.Time `json:"updated_at" example:"2023-01-01T00:00:00Z"`
}

type CreateProductResource struct {
	ImageURL    string  `json:"image_url" example:"https://example.com/image.jpg" validate:"required,url"`
	Title       string  `json:"title" example:"Product Title" validate:"required,min=1,max=255"`
	Price       float64 `json:"price" example:"99.99" validate:"required,min=0"`
	Description string  `json:"description" example:"Product description" validate:"max=1000"`
	Category    string  `json:"category" example:"Electronics" validate:"required"`
	Quantity    int     `json:"quantity" example:"10" validate:"required,min=0"`
}

type UpdateProductResource struct {
	ImageURL    *string  `json:"image_url,omitempty" example:"https://example.com/image.jpg" validate:"omitempty,url"`
	Title       *string  `json:"title,omitempty" example:"Product Title" validate:"omitempty,min=1,max=255"`
	Price       *float64 `json:"price,omitempty" example:"99.99" validate:"omitempty,min=0"`
	Description *string  `json:"description,omitempty" example:"Product description" validate:"omitempty,max=1000"`
	Category    *string  `json:"category,omitempty" example:"Electronics" validate:"omitempty"`
	Quantity    *int     `json:"quantity,omitempty" example:"10" validate:"omitempty,min=0"`
}

type ErrorResponse struct {
	Error string `json:"error" example:"error message"`
}
