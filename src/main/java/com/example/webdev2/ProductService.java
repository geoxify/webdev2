package com.example.webdev2;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    // Dependencies managed by the Spring IoC container
    private final ProductRepository productRepository;
    private final ShopProperties shopProperties;

    // Constructor-based Dependency Injection
    // Spring supplies both ProductRepository and ShopProperties beans
    public ProductService(ProductRepository productRepository, ShopProperties shopProperties) {
        this.productRepository = productRepository;
        this.shopProperties = shopProperties;
    }

    public String getShopName() {
        return shopProperties.getName();
    }

    public String getShopCurrency() {
        return shopProperties.getCurrency();
    }

    public ShopProperties getShopProperties() {
        return shopProperties;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsAboveThreshold(double threshold) {
        return productRepository.findAll().stream()
                .filter(product -> product.getPrice() > threshold)
                .collect(Collectors.toList());
    }

    // Format product info using externalized currency from application.properties
    public String formatProduct(Product product) {
        return String.format("%s - %s %.0f", product.getName(), shopProperties.getCurrency(), product.getPrice());
    }
}
