package com.example.webdev2;

import java.util.Objects;

/**
 * Product domain model representing an inventory item.
 * Supports form backing and data binding in Spring MVC and Thymeleaf.
 */
public class Product {

    private Long id;
    private String name;
    private Double price;
    private Category category;
    private String description;

    public Product() {
        // Initialize default category so nested property product.category.name is readily accessible
        this.category = new Category("General");
    }

    public Product(Long id, String name, Double price, Category category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = (category != null) ? category : new Category("General");
    }

    public Product(Long id, String name, Double price, Category category, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = (category != null) ? category : new Category("General");
        this.description = description;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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
                ", price=" + price +
                ", category=" + category +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) &&
                Objects.equals(name, product.name) &&
                Objects.equals(price, product.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price);
    }
}
