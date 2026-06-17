package com.vamshi.inventory;

import com.vamshi.inventory.model.Product;
import com.vamshi.inventory.repository.ProductRepository;
import com.vamshi.inventory.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        sampleProduct = new Product("Barcode Scanner", "ZBR-001", 50, 299.99, "Hardware");
        sampleProduct.setId(1L);
    }

    @Test
    void createProduct_success() {
        when(productRepository.findBySku("ZBR-001")).thenReturn(Optional.empty());
        when(productRepository.save(sampleProduct)).thenReturn(sampleProduct);

        Product result = productService.createProduct(sampleProduct);

        assertNotNull(result);
        assertEquals("ZBR-001", result.getSku());
        verify(productRepository, times(1)).save(sampleProduct);
    }

    @Test
    void createProduct_duplicateSku_throwsException() {
        when(productRepository.findBySku("ZBR-001")).thenReturn(Optional.of(sampleProduct));

        assertThrows(IllegalArgumentException.class,
                () -> productService.createProduct(sampleProduct));
        verify(productRepository, never()).save(any());
    }

    @Test
    void adjustStock_increase_success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        when(productRepository.save(any())).thenReturn(sampleProduct);

        Product result = productService.adjustStock(1L, 10);

        assertEquals(60, result.getQuantity());
    }

    @Test
    void adjustStock_belowZero_throwsException() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));

        assertThrows(IllegalArgumentException.class,
                () -> productService.adjustStock(1L, -100));
    }

    @Test
    void getProductById_notFound_returnsEmpty() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Product> result = productService.getProductById(99L);

        assertTrue(result.isEmpty());
    }

    @Test
    void deleteProduct_notFound_throwsException() {
        when(productRepository.existsById(99L)).thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> productService.deleteProduct(99L));
    }
}
