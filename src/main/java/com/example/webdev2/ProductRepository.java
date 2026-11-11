package com.example.webdev2;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Product persistence operations.
 * Defines contract for in-memory CRUD operations.
 */
public interface ProductRepository {

    /**
     * Retrieve all products from storage.
     *
     * @return list of all products
     */
    List<Product> findAll();

    /**
     * Find a product by its unique identifier.
     *
     * @param id product ID
     * @return Optional containing the product if found, or empty Optional
     */
    Optional<Product> findById(Long id);

    /**
     * Save a new product to storage and assign a unique ID.
     *
     * @param product product entity to save
     * @return persisted product with assigned ID
     */
    Product save(Product product);

    /**
     * Update an existing product in storage.
     *
     * @param id product ID
     * @param product updated product details
     * @return updated product
     */
    Product update(Long id, Product product);

    /**
     * Delete a product by its unique identifier.
     *
     * @param id product ID
     * @return true if deleted, false if not found
     */
    boolean deleteById(Long id);

    /**
     * Check if a product with the given ID exists.
     *
     * @param id product ID
     * @return true if exists, false otherwise
     */
    boolean existsById(Long id);

    /**
     * Reset storage with initial seed data.
     */
    void reset();

    /**
     * Clear all records from storage.
     */
    void clear();
}
