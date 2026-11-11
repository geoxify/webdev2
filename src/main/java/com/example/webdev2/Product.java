package com.example.webdev2;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;

/**
 * Product domain entity representing a retail or warehouse inventory item.
 * Applies Jakarta Bean Validation annotations to enforce data integrity.
 */
public class Product {

    private Long id;

    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Category is required")
    @Size(min = 2, max = 50, message = "Category must be between 2 and 50 characters")
    private String category;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be at least $0.01")
    @DecimalMax(value = "99999.99", message = "Price cannot exceed $99,999.99")
    private Double price;

    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity cannot be negative")
    @Max(value = 100000, message = "Stock quantity cannot exceed 100,000 units")
    private Integer stockQuantity;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    public Product() {
    }

    public Product(Long id, String name, String category, Double price, Integer stockQuantity, String description) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.description = description;
    }

    public Product(Long id, String name, String category, Double price, Integer stockQuantity) {
        this(id, name, category, price, stockQuantity, "");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) &&
                Objects.equals(name, product.name) &&
                Objects.equals(category, product.category) &&
                Objects.equals(price, product.price) &&
                Objects.equals(stockQuantity, product.stockQuantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, category, price, stockQuantity);
    }
}
