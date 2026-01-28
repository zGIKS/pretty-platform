package mercadopago

import (
	"bytes"
	"context"
	"encoding/json"
	"fmt"
	"net/http"
	"time"
)

type Client struct {
	accessToken string
	publicKey   string
	baseURL     string
	httpClient  *http.Client
}

type PreferenceItem struct {
	Title      string  `json:"title"`
	Quantity   int     `json:"quantity"`
	UnitPrice  float64 `json:"unit_price"`
	CurrencyID string  `json:"currency_id"`
}

type PreferencePayer struct {
	Email string `json:"email"`
}

type PreferenceBackURLs struct {
	Success string `json:"success"`
	Failure string `json:"failure"`
	Pending string `json:"pending"`
}

type PreferenceRequest struct {
	Items             []PreferenceItem   `json:"items"`
	Payer             PreferencePayer    `json:"payer"`
	BackURLs          PreferenceBackURLs `json:"back_urls"`
	AutoReturn        string             `json:"auto_return,omitempty"`
	NotificationURL   string             `json:"notification_url,omitempty"`
	ExternalReference string             `json:"external_reference,omitempty"`
}

type PreferenceResponse struct {
	ID               string `json:"id"`
	InitPoint        string `json:"init_point"`
	SandboxInitPoint string `json:"sandbox_init_point"`
}

func NewClient(accessToken, publicKey string) *Client {
	return &Client{
		accessToken: accessToken,
		publicKey:   publicKey,
		baseURL:     "https://api.mercadopago.com",
		httpClient:  &http.Client{Timeout: 10 * time.Second},
	}
}

func (c *Client) CreatePreference(ctx context.Context, request PreferenceRequest) (PreferenceResponse, error) {
	payload, err := json.Marshal(request)
	if err != nil {
		return PreferenceResponse{}, fmt.Errorf("failed to marshal preference payload: %w", err)
	}

	req, err := http.NewRequestWithContext(ctx, http.MethodPost, c.baseURL+"/checkout/preferences", bytes.NewReader(payload))
	if err != nil {
		return PreferenceResponse{}, fmt.Errorf("failed to build preference request: %w", err)
	}
	req.Header.Set("Authorization", "Bearer "+c.accessToken)
	req.Header.Set("Content-Type", "application/json")

	resp, err := c.httpClient.Do(req)
	if err != nil {
		return PreferenceResponse{}, fmt.Errorf("failed to call Mercado Pago preference endpoint: %w", err)
	}
	defer resp.Body.Close()

	if resp.StatusCode >= 300 {
		return PreferenceResponse{}, fmt.Errorf("mercado pago returned status %d", resp.StatusCode)
	}

	var mpResp PreferenceResponse
	if err := json.NewDecoder(resp.Body).Decode(&mpResp); err != nil {
		return PreferenceResponse{}, fmt.Errorf("failed to decode preference response: %w", err)
	}

	return mpResp, nil
}

func (c *Client) PublicKey() string {
	return c.publicKey
}
