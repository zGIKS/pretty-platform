package events

import (
	"go-service/internal/products/domain/model/valueobjects"
	"time"
)

type ProductCreatedEvent struct {
	productID  valueobjects.ProductID
	occurredOn time.Time
}

func NewProductCreatedEvent(productID valueobjects.ProductID) ProductCreatedEvent {
	return ProductCreatedEvent{
		productID:  productID,
		occurredOn: time.Now(),
	}
}

func (e ProductCreatedEvent) ProductID() valueobjects.ProductID { return e.productID }
func (e ProductCreatedEvent) OccurredOn() time.Time             { return e.occurredOn }
