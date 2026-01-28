package valueobjects

import "errors"

type PaymentStatus struct {
	value string `gorm:"column:status"`
}

const (
	PaymentStatusPending = "pending"
	PaymentStatusPaid    = "paid"
	PaymentStatusFailed  = "failed"
)

func NewPaymentStatus(value string) (PaymentStatus, error) {
	if value == "" {
		return PaymentStatus{}, errors.New("payment status cannot be empty")
	}
	switch value {
	case PaymentStatusPending, PaymentStatusPaid, PaymentStatusFailed:
		return PaymentStatus{value: value}, nil
	default:
		return PaymentStatus{}, errors.New("unsupported payment status")
	}
}

func (p PaymentStatus) Value() string {
	return p.value
}
