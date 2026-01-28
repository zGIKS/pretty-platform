package events

import (
	"time"

	"go-service/internal/payments/domain/model/valueobjects"
)

type PaymentCreatedEvent struct {
	paymentID  valueobjects.PaymentID
	status     valueobjects.PaymentStatus
	occurredOn time.Time
}

func NewPaymentCreatedEvent(paymentID valueobjects.PaymentID, status valueobjects.PaymentStatus) PaymentCreatedEvent {
	return PaymentCreatedEvent{
		paymentID:  paymentID,
		status:     status,
		occurredOn: time.Now(),
	}
}

func (e PaymentCreatedEvent) PaymentID() valueobjects.PaymentID  { return e.paymentID }
func (e PaymentCreatedEvent) Status() valueobjects.PaymentStatus { return e.status }
func (e PaymentCreatedEvent) OccurredOn() time.Time              { return e.occurredOn }
