package com.example.webdev2;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

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
