package main

import (
	"fmt"
	"go-service/docs"
	"go-service/internal/config"
	"go-service/internal/products/application/commandservices"
	"go-service/internal/products/application/queryservices"
	"go-service/internal/products/domain/model/entities"
	"go-service/internal/products/infrastructure/persistence/repositories"
	"go-service/internal/products/interfaces/rest/controllers"
	"os"

	"github.com/gofiber/fiber/v2"
	"github.com/joho/godotenv"
	fiberSwagger "github.com/swaggo/fiber-swagger"
)

// @title Products API
// @version 1.0
// @description This is a products management API.
// @host localhost:3000
// @BasePath /
func main() {
	godotenv.Load()

	port := os.Getenv("PORT")
	if port == "" {
		port = "3000"
	}

	docs.SwaggerInfo.Host = "localhost:" + port

	// Load config and init DB
	cfg := config.LoadConfig()
	db := config.InitDB(cfg)

	// Auto migrate
	db.AutoMigrate(&entities.Product{})

	// Wire dependencies
	productRepo := repositories.NewProductRepository(db)
	productCommandService := commandservices.NewProductCommandService(productRepo)
	productQueryService := queryservices.NewProductQueryService(productRepo)
	productController := controllers.NewProductController(productCommandService, productQueryService)

	app := fiber.New()

	// Routes
	app.Post("/products", productController.CreateProduct)
	app.Get("/products", productController.GetAllProducts)
	app.Get("/products/:id", productController.GetProduct)
	app.Put("/products/:id", productController.UpdateProduct)
	app.Delete("/products/:id", productController.DeleteProduct)

	app.Get("/swagger-ui/*", fiberSwagger.WrapHandler)

	fmt.Println("Swagger UI disponible en http://localhost:" + port + "/swagger-ui/")

	app.Listen(":" + port)
}
