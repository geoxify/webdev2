package com.example.webdev2;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Service layer coordinating Product business operations and validation logic.
 * Enforces separation of concerns by isolating business rules from web and persistence layers.
 */
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Retrieve all products in the catalog.
     *
     * @return list of products
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Find a product by its unique identifier.
     *
     * @param id product ID
     * @return Optional containing the product if found
     */
    public Optional<Product> getProductById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return productRepository.findById(id);
    }

    /**
     * Create a new product.
     *
     * @param product product entity to create
     * @return persisted product with assigned ID
     */
    public Product createProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        return productRepository.save(product);
    }

    /**
     * Update an existing product.
     *
     * @param id product ID
     * @param product updated product details
     * @return updated product
     * @throws NoSuchElementException if product with ID does not exist
     */
    public Product updateProduct(Long id, Product product) {
        if (id == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        if (product == null) {
            throw new IllegalArgumentException("Product payload cannot be null");
        }
        if (!productRepository.existsById(id)) {
            throw new NoSuchElementException("Cannot update product: Product with ID #" + id + " does not exist.");
        }
        return productRepository.update(id, product);
    }

    /**
     * Delete a product by its unique identifier.
     *
     * @param id product ID
     * @return true if deleted, false if not found
     */
    public boolean deleteProduct(Long id) {
        if (id == null) {
            return false;
        }
        return productRepository.deleteById(id);
    }

    /**
     * Check if a product with the specified ID exists.
     *
     * @param id product ID
     * @return true if exists, false otherwise
     */
    public boolean existsById(Long id) {
        return id != null && productRepository.existsById(id);
    }

    /**
     * Reset the catalog back to initial sample items.
     */
    public void resetCatalog() {
        productRepository.reset();
    }

    /**
     * Clear all products from catalog.
     */
    public void clearCatalog() {
        productRepository.clear();
    }
}
