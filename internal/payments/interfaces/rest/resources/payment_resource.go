package resources

import "time"

type PaymentResource struct {
	ID            string    `json:"id" example:"550e8400-e29b-41d4-a716-446655440000"`
	ProductID     string    `json:"product_id" example:"550e8400-e29b-41d4-a716-446655440000"`
	ProductTitle  string    `json:"product_title" example:"Special Product"`
	Amount        float64   `json:"amount" example:"199.99"`
	Currency      string    `json:"currency" example:"ARS"`
	Quantity      int       `json:"quantity" example:"1"`
	PreferenceID  string    `json:"preference_id" example:"123456789"`
	PreferenceURL string    `json:"preference_url" example:"https://www.mercadopago.com/checkout/v1/redirect?pref_id=123"`
	Status        string    `json:"status" example:"pending"`
	CreatedAt     time.Time `json:"created_at" example:"2024-01-01T00:00:00Z"`
	UpdatedAt     time.Time `json:"updated_at" example:"2024-01-01T00:00:00Z"`
}

type CreatePaymentResource struct {
	ProductID  string `json:"product_id" example:"550e8400-e29b-41d4-a716-446655440000" validate:"required,uuid"`
	Quantity   int    `json:"quantity" example:"1" validate:"required,min=1"`
	PayerEmail string `json:"payer_email" example:"buyer@example.com" validate:"required,email"`
}

type PaymentCreationResponse struct {
	ID            string  `json:"id" example:"550e8400-e29b-41d4-a716-446655440000"`
	PreferenceID  string  `json:"preference_id" example:"123456"`
	PreferenceURL string  `json:"preference_url" example:"https://www.mercadopago.com/checkout/v1/redirect?pref_id=123"`
	Status        string  `json:"status" example:"pending"`
	PublicKey     string  `json:"public_key" example:"APP_USR-xxxxxxxx"`
	Amount        float64 `json:"amount" example:"199.99"`
	Currency      string  `json:"currency" example:"ARS"`
	Quantity      int     `json:"quantity" example:"1"`
	ProductTitle  string  `json:"product_title" example:"Special Product"`
}

type ErrorResponse struct {
	Error string `json:"error" example:"error message"`
}
