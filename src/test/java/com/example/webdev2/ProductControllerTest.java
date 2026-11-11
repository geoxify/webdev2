package com.example.webdev2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ProductController()).build();
    }

    @Test
    @DisplayName("Task 1: GET /products returns view 'products' with list of products")
    void testGetAllProducts() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(view().name("products"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("products", hasSize(greaterThanOrEqualTo(2))));
    }

    @Test
    @DisplayName("Task 1: GET /products?empty=true returns empty list showing th:if empty state")
    void testGetEmptyProducts() throws Exception {
        mockMvc.perform(get("/products").param("empty", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("products"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("products", hasSize(0)));
    }

    @Test
    @DisplayName("Task 2: GET /products/{id} displays product detail view with model attribute")
    void testGetProductDetail() throws Exception {
        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("product-detail"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attribute("product", hasProperty("name", is("Mechanical Keyboard"))))
                .andExpect(model().attribute("product", hasProperty("category", hasProperty("name", is("Peripherals")))));
    }

    @Test
    @DisplayName("Task 4: GET /products/new displays product-form with backing object")
    void testShowProductForm() throws Exception {
        mockMvc.perform(get("/products/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("product-form"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    @DisplayName("Task 4: POST /products with valid data saves and redirects to /products (PRG pattern)")
    void testCreateProductSuccess() throws Exception {
        mockMvc.perform(post("/products")
                        .param("name", "Noise Cancelling Headphones")
                        .param("price", "199.99")
                        .param("category.name", "Audio")
                        .param("description", "Premium active noise cancellation headphones"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));
    }

    @Test
    @DisplayName("Task 5: POST /products with invalid price redisplays form with BindingResult error")
    void testCreateProductBindingError() throws Exception {
        mockMvc.perform(post("/products")
                        .param("name", "Invalid Price Gadget")
                        .param("price", "not-a-number")
                        .param("category.name", "Gadgets"))
                .andExpect(status().isOk())
                .andExpect(view().name("product-form"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("product", "price"));
    }
}
