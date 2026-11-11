package com.example.webdev2;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    // Dependency managed by Spring IoC container
    private final ProductRepository productRepository;

    // Constructor-based Dependency Injection
    // Spring IoC container automatically provides the ProductRepository bean
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsAboveThreshold(double threshold) {
        return productRepository.findAll().stream()
                .filter(product -> product.getPrice() > threshold)
                .collect(Collectors.toList());
    }
}
