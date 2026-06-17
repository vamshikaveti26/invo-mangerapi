package com.vamshi.inventory.controller;

import com.vamshi.inventory.model.Product;
import com.vamshi.inventory.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // GET /api/products — list all, or filter by category/name
    @GetMapping
    public ResponseEntity<List<Product>> getProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search) {

        if (category != null) {
            return ResponseEntity.ok(productService.getProductsByCategory(category));
        }
        if (search != null) {
            return ResponseEntity.ok(productService.searchByName(search));
        }
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // GET /api/products/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/products/sku/{sku}
    @GetMapping("/sku/{sku}")
    public ResponseEntity<Product> getProductBySku(@PathVariable String sku) {
        return productService.getProductBySku(sku)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/products/low-stock?threshold=10
    @GetMapping("/low-stock")
    public ResponseEntity<List<Product>> getLowStock(
            @RequestParam(defaultValue = "10") int threshold) {
        return ResponseEntity.ok(productService.getLowStockProducts(threshold));
    }

    // POST /api/products
    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody Product product) {
        try {
            Product created = productService.createProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // PUT /api/products/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id,
                                            @Valid @RequestBody Product product) {
        try {
            return ResponseEntity.ok(productService.updateProduct(id, product));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // PATCH /api/products/{id}/stock?delta=5  (adjust stock up or down)
    @PatchMapping("/{id}/stock")
    public ResponseEntity<?> adjustStock(@PathVariable Long id,
                                          @RequestParam int delta) {
        try {
            return ResponseEntity.ok(productService.adjustStock(id, delta));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/products/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
