package main

import (
	"fmt"
	"go-service/docs"
	"go-service/internal/config"
	paymentsCommandServices "go-service/internal/payments/application/commandservices"
	paymentsQueryServices "go-service/internal/payments/application/queryservices"
	paymentEntities "go-service/internal/payments/domain/model/entities"
	"go-service/internal/payments/infrastructure/mercadopago"
	paymentRepositories "go-service/internal/payments/infrastructure/persistence/repositories"
	paymentControllers "go-service/internal/payments/interfaces/rest/controllers"
	"go-service/internal/products/application/commandservices"
	"go-service/internal/products/application/queryservices"
	productEntities "go-service/internal/products/domain/model/entities"
	"go-service/internal/products/infrastructure/persistence/repositories"
	productacl "go-service/internal/products/interfaces/acl"
	"go-service/internal/products/interfaces/rest/controllers"
	"os"
	"strings"

	"github.com/gofiber/fiber/v2"
	"github.com/gofiber/fiber/v2/middleware/cors"
	"github.com/gofiber/fiber/v2/middleware/recover"
	"github.com/joho/godotenv"
	fiberSwagger "github.com/swaggo/fiber-swagger"
)

// @title Products API
// @version 1.0
// @description This is a products management API.
// @host localhost:3000
// @BasePath /api/v1
func main() {
	godotenv.Load()

	port := os.Getenv("PORT")
	if port == "" {
		port = "3000"
	}

	docs.SwaggerInfo.Host = ""
	docs.SwaggerInfo.SwaggerTemplate = strings.Replace(docs.SwaggerInfo.SwaggerTemplate, "\"host\": \"{{.Host}}\",", "", -1)
	docs.SwaggerInfo.BasePath = "/api/v1"

	// Load config and init DB
	cfg := config.LoadConfig()
	db := config.InitDB(cfg)

	// Auto migrate
	db.AutoMigrate(&productEntities.Product{}, &paymentEntities.Payment{})

	// Wire dependencies
	productRepo := repositories.NewProductRepository(db)
	productCommandService := commandservices.NewProductCommandService(productRepo)
	productQueryService := queryservices.NewProductQueryService(productRepo)
	productController := controllers.NewProductController(productCommandService, productQueryService)
	productPaymentsFacade := productacl.NewProductPaymentFacade(productQueryService, cfg.DefaultCurrency)

	paymentRepo := paymentRepositories.NewPaymentRepository(db)
	mpClient := mercadopago.NewClient(cfg.MercadoPagoAccessToken, cfg.MercadoPagoPublicKey)
	paymentCommandService := paymentsCommandServices.NewPaymentCommandService(paymentRepo, mpClient, productPaymentsFacade, cfg.FrontendBaseURL, cfg.PaymentsNotificationURL)
	paymentQueryService := paymentsQueryServices.NewPaymentQueryService(paymentRepo)
	paymentController := paymentControllers.NewPaymentController(paymentCommandService, paymentQueryService, mpClient)

	app := fiber.New()
	app.Use(recover.New()) // Recover from panics to avoid network errors
	app.Use(cors.New(cors.Config{
		AllowOrigins:     cfg.CorsAllowOrigins,
		AllowMethods:     cfg.CorsAllowMethods,
		AllowHeaders:     cfg.CorsAllowHeaders,
		ExposeHeaders:    cfg.CorsExposeHeaders,
		AllowCredentials: cfg.CorsAllowCredentials,
		MaxAge:           cfg.CorsMaxAge,
	}))

	// Routes
	api := app.Group("/api/v1")
	api.Post("/products", productController.CreateProduct)
	api.Get("/products", productController.GetAllProducts)
	api.Get("/products/:id", productController.GetProduct)
	api.Put("/products/:id", productController.UpdateProduct)
	api.Delete("/products/:id", productController.DeleteProduct)
	api.Post("/payments", paymentController.CreatePayment)
	api.Get("/payments/:id", paymentController.GetPayment)

	app.Get("/swagger-ui/*", fiberSwagger.WrapHandler)

	fmt.Println("Swagger UI disponible en http://localhost:" + port + "/swagger-ui/")

	app.Listen(":" + port)
}
