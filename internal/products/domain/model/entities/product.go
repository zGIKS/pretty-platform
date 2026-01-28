package entities

import (
	"go-service/internal/products/domain/model/valueobjects"
	"time"

	"github.com/google/uuid"
)

type Product struct {
	ID          uuid.UUID `gorm:"type:uuid;primaryKey;column:id"`
	ImageURL    string    `gorm:"column:image_url"`
	Title       string    `gorm:"column:title"`
	Price       float64   `gorm:"column:price"`
	Description string    `gorm:"column:description"`
	Category    string    `gorm:"column:category"`
	Quantity    int       `gorm:"column:quantity"`
	CreatedAt   time.Time `gorm:"column:created_at"`
	UpdatedAt   time.Time `gorm:"column:updated_at"`
}

func NewProduct(
	imageURL valueobjects.ImageURL,
	title valueobjects.Title,
	price valueobjects.Price,
	description valueobjects.Description,
	category valueobjects.Category,
	quantity valueobjects.Quantity,
) (*Product, error) {
	now := time.Now()
	productID := uuid.New()
	return &Product{
		ID:          productID,
		ImageURL:    imageURL.Value(),
		Title:       title.Value(),
		Price:       price.Value(),
		Description: description.Value(),
		Category:    category.Value(),
		Quantity:    quantity.Value(),
		CreatedAt:   now,
		UpdatedAt:   now,
	}, nil
}

func (p *Product) GetID() valueobjects.ProductID {
	return valueobjects.NewProductIDFromUUID(p.ID)
}

func (p *Product) GetImageURL() valueobjects.ImageURL {
	v, _ := valueobjects.NewImageURL(p.ImageURL)
	return v
}

func (p *Product) GetTitle() valueobjects.Title {
	v, _ := valueobjects.NewTitle(p.Title)
	return v
}

func (p *Product) GetPrice() valueobjects.Price {
	v, _ := valueobjects.NewPrice(p.Price)
	return v
}

func (p *Product) GetDescription() valueobjects.Description {
	v, _ := valueobjects.NewDescription(p.Description)
	return v
}

func (p *Product) GetCategory() valueobjects.Category {
	v, _ := valueobjects.NewCategory(p.Category)
	return v
}

func (p *Product) GetQuantity() valueobjects.Quantity {
	v, _ := valueobjects.NewQuantity(p.Quantity)
	return v
}

func (p *Product) GetCreatedAt() time.Time {
	return p.CreatedAt
}

func (p *Product) GetUpdatedAt() time.Time {
	return p.UpdatedAt
}

func (p *Product) Update(
	imageURL *valueobjects.ImageURL,
	title *valueobjects.Title,
	price *valueobjects.Price,
	description *valueobjects.Description,
	category *valueobjects.Category,
	quantity *valueobjects.Quantity,
) {
	if imageURL != nil {
		p.ImageURL = imageURL.Value()
	}
	if title != nil {
		p.Title = title.Value()
	}
	if price != nil {
		p.Price = price.Value()
	}
	if description != nil {
		p.Description = description.Value()
	}
	if category != nil {
		p.Category = category.Value()
	}
	if quantity != nil {
		p.Quantity = quantity.Value()
	}
	p.UpdatedAt = time.Now()
}
