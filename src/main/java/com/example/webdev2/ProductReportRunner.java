package com.example.webdev2;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductReportRunner implements CommandLineRunner {

    private final ProductService productService;

    // Constructor injection: Spring provides the ProductService bean
    public ProductReportRunner(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void run(String... args) throws Exception {
        double threshold = 5000.0;
        List<Product> filteredProducts = productService.getProductsAboveThreshold(threshold);

        System.out.println();
        System.out.println("================================");
        System.out.println("       PRODUCT REPORT");
        System.out.println("================================");
        System.out.println("Shop: " + productService.getShopName());
        System.out.println("Currency: " + productService.getShopCurrency());
        System.out.println();
        System.out.printf("Products above %s %.0f:%n%n", productService.getShopCurrency(), threshold);

        for (Product product : filteredProducts) {
            System.out.println(productService.formatProduct(product));
        }

        System.out.println("================================");
        System.out.println();
    }
}
