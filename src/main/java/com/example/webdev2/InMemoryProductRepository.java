package com.example.webdev2;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Thread-safe in-memory repository implementation using ConcurrentHashMap.
 * Encapsulates low-level persistence operations.
 */
@Repository
public class InMemoryProductRepository implements ProductRepository {

    private final Map<Long, Product> storage = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(0);

    public InMemoryProductRepository() {
        reset();
    }

    @Override
    public List<Product> findAll() {
        List<Product> list = new ArrayList<>(storage.values());
        list.sort(Comparator.comparing(Product::getId));
        return list;
    }

    @Override
    public Optional<Product> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Product save(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (product.getId() == null || product.getId() <= 0) {
            product.setId(idSequence.incrementAndGet());
        } else {
            // Keep idSequence in sync if manual ID provided
            idSequence.accumulateAndGet(product.getId(), Math::max);
        }
        storage.put(product.getId(), product);
        return product;
    }

    @Override
    public Product update(Long id, Product product) {
        if (id == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        if (product == null) {
            throw new IllegalArgumentException("Product payload cannot be null");
        }
        if (!storage.containsKey(id)) {
            throw new NoSuchElementException("Product with ID #" + id + " does not exist.");
        }
        product.setId(id);
        storage.put(id, product);
        return product;
    }

    @Override
    public boolean deleteById(Long id) {
        if (id == null) {
            return false;
        }
        return storage.remove(id) != null;
    }

    @Override
    public boolean existsById(Long id) {
        if (id == null) {
            return false;
        }
        return storage.containsKey(id);
    }

    @Override
    public void reset() {
        storage.clear();
        idSequence.set(0);

        save(new Product(1L, "Mechanical Keyboard", "Peripherals", 129.99, 45, "Tactile hot-swappable mechanical keyboard with RGB backlighting"));
        save(new Product(2L, "UltraWide Monitor", "Electronics", 449.50, 18, "34-inch curved ultra-wide IPS productivity monitor"));
        save(new Product(3L, "Ergonomic Office Chair", "Furniture", 289.00, 12, "High-back mesh ergonomic desk chair with adjustable lumbar support"));
        save(new Product(4L, "Wireless Gaming Mouse", "Peripherals", 79.99, 60, "Low-latency wireless precision mouse with dual optical sensors"));
        save(new Product(5L, "USB-C Multiport Dock", "Electronics", 89.95, 30, "10-in-1 aluminum USB-C hub with dual HDMI and 100W Power Delivery"));
    }

    @Override
    public void clear() {
        storage.clear();
    }
}
