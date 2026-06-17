# Inventory Management REST API

A Spring Boot REST API for managing product inventory — designed to simulate the kind of internal tooling used in barcode scanner and hardware distribution workflows.

## Tech Stack

- **Java 17** + **Spring Boot 3.2**
- **Spring Data JPA** (H2 in-memory for dev, swap for PostgreSQL/MySQL in prod)
- **Bean Validation** (jakarta.validation)
- **JUnit 5** + **Mockito** for unit tests
- **Maven** for build/dependency management

## Running the App

```bash
mvn spring-boot:run
```

API available at `http://localhost:8080/api/products`

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/products` | List all products |
| GET | `/api/products?category=Hardware` | Filter by category |
| GET | `/api/products?search=scanner` | Search by name |
| GET | `/api/products/{id}` | Get product by ID |
| GET | `/api/products/sku/{sku}` | Lookup by SKU |
| GET | `/api/products/low-stock?threshold=10` | Find low-stock items |
| POST | `/api/products` | Create a new product |
| PUT | `/api/products/{id}` | Update a product |
| PATCH | `/api/products/{id}/stock?delta=5` | Adjust stock (+ or -) |
| DELETE | `/api/products/{id}` | Delete a product |

## Sample Request

```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Barcode Scanner ZQ620","sku":"ZBR-001","quantity":50,"price":299.99,"category":"Hardware"}'
```

## Running Tests

```bash
mvn test
```

6 unit tests covering: create, duplicate SKU validation, stock adjustment, negative stock guard, not-found handling.

## Project Structure

```
src/
├── main/java/com/vamshi/inventory/
│   ├── model/         Product.java
│   ├── repository/    ProductRepository.java
│   ├── service/       ProductService.java
│   └── controller/    ProductController.java
└── test/java/com/vamshi/inventory/
    └── ProductServiceTest.java
```
