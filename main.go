package main

import (
	"fmt"
	"go-service/docs"
	"os"

	"github.com/gofiber/fiber/v2"
	"github.com/joho/godotenv"
	fiberSwagger "github.com/swaggo/fiber-swagger"
)

// @title Hello World API
// @version 1.0
// @description This is a sample server.
// @host localhost:3000
// @BasePath /
func main() {
	godotenv.Load()

	port := os.Getenv("PORT")
	if port == "" {
		port = "3000"
	}

	docs.SwaggerInfo.Host = "localhost:" + port

	app := fiber.New()

	app.Get("/hello", helloWorld)

	app.Get("/swagger-ui/*", fiberSwagger.WrapHandler)

	fmt.Println("Swagger UI disponible en http://localhost:" + port + "/swagger-ui/")

	app.Listen(":" + port)
}

// helloWorld godoc
// @Summary Get hello world
// @Description get hello world
// @Tags hello
// @Accept  json
// @Produce  json
// @Success 200 {string} string "hello world"
// @Router /hello [get]
func helloWorld(c *fiber.Ctx) error {
	return c.SendString("hello world")
}
