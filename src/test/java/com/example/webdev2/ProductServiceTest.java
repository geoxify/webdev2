package com.example.webdev2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

    private InMemoryProductRepository repository;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        repository = new InMemoryProductRepository();
        productService = new ProductService(repository);
    }

    @Test
    @DisplayName("Task 3: Service retrieves all products from repository")
    void testGetAllProducts() {
        List<Product> products = productService.getAllProducts();
        assertNotNull(products);
        assertEquals(5, products.size());
    }

    @Test
    @DisplayName("Task 3: Service finds product by existing ID")
    void testGetProductByIdFound() {
        Optional<Product> productOpt = productService.getProductById(1L);
        assertTrue(productOpt.isPresent());
        assertEquals("Mechanical Keyboard", productOpt.get().getName());
    }

    @Test
    @DisplayName("Task 3: Service returns empty Optional for non-existent ID")
    void testGetProductByIdNotFound() {
        Optional<Product> productOpt = productService.getProductById(999L);
        assertTrue(productOpt.isEmpty());
    }

    @Test
    @DisplayName("Task 3: Service creates and assigns unique ID to new product")
    void testCreateProduct() {
        Product newProduct = new Product(null, "4K Webcam", "Peripherals", 89.99, 15, "Ultra HD webcam with autofocus");
        Product created = productService.createProduct(newProduct);

        assertNotNull(created.getId());
        assertTrue(created.getId() > 0);
        assertEquals("4K Webcam", created.getName());
        assertTrue(productService.existsById(created.getId()));
    }

    @Test
    @DisplayName("Task 3: Service updates existing product details")
    void testUpdateProduct() {
        Product updatedDetails = new Product(1L, "Mechanical Keyboard Pro", "Peripherals", 149.99, 30, "Updated description");
        Product result = productService.updateProduct(1L, updatedDetails);

        assertEquals("Mechanical Keyboard Pro", result.getName());
        assertEquals(149.99, result.getPrice());
        assertEquals(30, result.getStockQuantity());
    }

    @Test
    @DisplayName("Task 3: Service throws NoSuchElementException when updating non-existent product")
    void testUpdateProductNotFound() {
        Product nonExistent = new Product(999L, "Ghost Product", "Electronics", 10.0, 1);
        assertThrows(NoSuchElementException.class, () -> productService.updateProduct(999L, nonExistent));
    }

    @Test
    @DisplayName("Task 3: Service deletes product by ID")
    void testDeleteProduct() {
        assertTrue(productService.existsById(2L));
        boolean deleted = productService.deleteProduct(2L);
        assertTrue(deleted);
        assertFalse(productService.existsById(2L));
    }

    @Test
    @DisplayName("Task 3: Service returns false when deleting non-existent ID")
    void testDeleteProductNotFound() {
        boolean deleted = productService.deleteProduct(999L);
        assertFalse(deleted);
    }
}
