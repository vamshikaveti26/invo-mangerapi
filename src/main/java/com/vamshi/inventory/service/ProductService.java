package com.vamshi.inventory.service;

import com.vamshi.inventory.model.Product;
import com.vamshi.inventory.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Optional<Product> getProductBySku(String sku) {
        return productRepository.findBySku(sku);
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Product> getLowStockProducts(int threshold) {
        return productRepository.findLowStockProducts(threshold);
    }

    public Product createProduct(Product product) {
        if (productRepository.findBySku(product.getSku()).isPresent()) {
            throw new IllegalArgumentException("SKU already exists: " + product.getSku());
        }
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        existing.setName(updatedProduct.getName());
        existing.setQuantity(updatedProduct.getQuantity());
        existing.setPrice(updatedProduct.getPrice());
        existing.setCategory(updatedProduct.getCategory());
        // SKU is intentionally not updatable once created
        return productRepository.save(existing);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    public Product adjustStock(Long id, int delta) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        int newQuantity = product.getQuantity() + delta;
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Insufficient stock. Current: " + product.getQuantity());
        }
        product.setQuantity(newQuantity);
        return productRepository.save(product);
    }
}
