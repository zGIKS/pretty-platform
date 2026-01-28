package config

import (
	"log"
	"os"

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
	FrontendBaseURL         string
	PaymentsNotificationURL string
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
		DefaultCurrency:         getEnv("DEFAULT_CURRENCY", "ARS"),
		MercadoPagoPublicKey:    getEnv("MERCADO_PAGO_PUBLIC_KEY", ""),
		MercadoPagoAccessToken:  getEnv("MERCADO_PAGO_ACCESS_TOKEN", ""),
		FrontendBaseURL:         getEnv("FRONTEND_BASE_URL", "http://localhost:5173"),
		PaymentsNotificationURL: getEnv("PAYMENTS_NOTIFICATION_URL", ""),
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

	return cfg
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}

func InitDB(cfg *Config) *gorm.DB {
	dsn := "host=" + cfg.DBHost + " user=" + cfg.DBUser + " password=" + cfg.DBPassword + " dbname=" + cfg.DBName + " port=" + cfg.DBPort + " sslmode=disable"
	db, err := gorm.Open(postgres.Open(dsn), &gorm.Config{})
	if err != nil {
		log.Fatal("Failed to connect to database:", err)
	}

	return db
}
