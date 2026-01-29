package config

import (
	"log"
	"net/url"
	"os"
	"strconv"
	"strings"

	"github.com/joho/godotenv"
	"gorm.io/driver/postgres"
	"gorm.io/gorm"
)

type Config struct {
	DBHost                  string
	DBPort                  string
	DBUser                  string
	DBPassword              string
	DBName                  string
	DefaultCurrency         string
	MercadoPagoPublicKey    string
	MercadoPagoAccessToken  string
	PublicBaseURL           string
	FrontendBaseURL         string
	PaymentsNotificationURL string
	CorsAllowOrigins        string
	CorsAllowMethods        string
	CorsAllowHeaders        string
	CorsExposeHeaders       string
	CorsAllowCredentials    bool
	CorsMaxAge              int
}

func LoadConfig() *Config {
	if err := godotenv.Load(); err != nil {
		log.Println("No .env file found")
	}

	cfg := &Config{
		DBHost:                  getEnv("DB_HOST", ""),
		DBPort:                  getEnv("DB_PORT", ""),
		DBUser:                  getEnv("DB_USER", ""),
		DBPassword:              getEnv("DB_PASSWORD", ""),
		DBName:                  getEnv("DB_NAME", ""),
		DefaultCurrency:         getEnv("DEFAULT_CURRENCY", ""),
		MercadoPagoPublicKey:    getEnv("MERCADO_PAGO_PUBLIC_KEY", ""),
		MercadoPagoAccessToken:  getEnv("MERCADO_PAGO_ACCESS_TOKEN", ""),
		PublicBaseURL:           getEnv("PUBLIC_BASE_URL", ""),
		FrontendBaseURL:         getEnv("FRONTEND_BASE_URL", ""),
		PaymentsNotificationURL: getEnv("PAYMENTS_NOTIFICATION_URL", ""),
		CorsAllowOrigins:        getEnv("CORS_ALLOW_ORIGINS", ""),
		CorsAllowMethods:        getEnv("CORS_ALLOW_METHODS", "GET,POST,PUT,DELETE,OPTIONS"),
		CorsAllowHeaders:        getEnv("CORS_ALLOW_HEADERS", "Origin, Content-Type, Accept, Authorization"),
		CorsExposeHeaders:       getEnv("CORS_EXPOSE_HEADERS", ""),
		CorsAllowCredentials:    getEnvAsBool("CORS_ALLOW_CREDENTIALS", false),
		CorsMaxAge:              getEnvAsInt("CORS_MAX_AGE", 0),
	}

	// Validate required fields
	if cfg.DBHost == "" {
		log.Fatal("DB_HOST is required")
	}
	if cfg.DBPort == "" {
		log.Fatal("DB_PORT is required")
	}
	if cfg.DBUser == "" {
		log.Fatal("DB_USER is required")
	}
	if cfg.DBPassword == "" {
		log.Fatal("DB_PASSWORD is required")
	}
	if cfg.DBName == "" {
		log.Fatal("DB_NAME is required")
	}
	if cfg.MercadoPagoPublicKey == "" {
		log.Fatal("MERCADO_PAGO_PUBLIC_KEY is required")
	}
	if cfg.MercadoPagoAccessToken == "" {
		log.Fatal("MERCADO_PAGO_ACCESS_TOKEN is required")
	}
	if cfg.DefaultCurrency == "" {
		log.Fatal("DEFAULT_CURRENCY is required")
	}
	if cfg.PublicBaseURL == "" {
		log.Fatal("PUBLIC_BASE_URL is required")
	}
	if !isValidAbsoluteURL(cfg.PublicBaseURL) {
		log.Fatal("PUBLIC_BASE_URL must be an absolute URL (example: https://example.com)")
	}
	if strings.TrimSpace(cfg.FrontendBaseURL) != "" && !isValidAbsoluteURL(cfg.FrontendBaseURL) {
		log.Fatal("FRONTEND_BASE_URL must be an absolute URL (example: https://example.com)")
	}
	if strings.TrimSpace(cfg.PaymentsNotificationURL) == "" {
		cfg.PaymentsNotificationURL = strings.TrimRight(cfg.PublicBaseURL, "/") + "/api/v1/payments/notifications"
	}
	if !isValidAbsoluteURL(cfg.PaymentsNotificationURL) {
		log.Fatal("PAYMENTS_NOTIFICATION_URL must be an absolute URL (example: https://example.com/api/v1/payments/notifications)")
	}
	if cfg.CorsAllowOrigins == "" {
		log.Fatal("CORS_ALLOW_ORIGINS is required")
	}

	return cfg
}

func isValidAbsoluteURL(raw string) bool {
	raw = strings.TrimSpace(raw)
	u, err := url.Parse(raw)
	if err != nil {
		return false
	}
	return u.Scheme != "" && u.Host != ""
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}

func getEnvAsBool(key string, defaultValue bool) bool {
	value := os.Getenv(key)
	if value == "" {
		return defaultValue
	}
	parsed, err := strconv.ParseBool(value)
	if err != nil {
		return defaultValue
	}
	return parsed
}

func getEnvAsInt(key string, defaultValue int) int {
	value := os.Getenv(key)
	if value == "" {
		return defaultValue
	}
	parsed, err := strconv.Atoi(value)
	if err != nil {
		return defaultValue
	}
	return parsed
}

func InitDB(cfg *Config) *gorm.DB {
	dsn := "host=" + cfg.DBHost + " user=" + cfg.DBUser + " password=" + cfg.DBPassword + " dbname=" + cfg.DBName + " port=" + cfg.DBPort + " sslmode=disable"
	db, err := gorm.Open(postgres.Open(dsn), &gorm.Config{})
	if err != nil {
		log.Fatal("Failed to connect to database:", err)
	}

	return db
}
