package com.example.webdev2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductControllerTest {

    private MockMvc mockMvc;
    private InMemoryProductRepository repository;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        repository = new InMemoryProductRepository();
        productService = new ProductService(repository);
        ProductController controller = new ProductController(productService);

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/templates/");
        viewResolver.setSuffix(".html");

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    @DisplayName("Task 4 (Read): GET /products returns view 'products' with list of products")
    void testGetAllProducts() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(view().name("products"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("products", hasSize(greaterThanOrEqualTo(2))));
    }

    @Test
    @DisplayName("Task 4 (Read): GET /products?empty=true returns empty list")
    void testGetEmptyProducts() throws Exception {
        mockMvc.perform(get("/products").param("empty", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("products"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("products", hasSize(0)));
    }

    @Test
    @DisplayName("Task 4 (Read): GET /products/{id} displays product detail view")
    void testGetProductDetailFound() throws Exception {
        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("product-detail"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attribute("product", hasProperty("name", is("Mechanical Keyboard"))))
                .andExpect(model().attribute("product", hasProperty("category", is("Peripherals"))));
    }

    @Test
    @DisplayName("Task 4 (Read): GET /products/{id} with invalid ID redirects to /products with flash error")
    void testGetProductDetailNotFound() throws Exception {
        mockMvc.perform(get("/products/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"))
                .andExpect(flash().attributeExists("errorMessage"));
    }

    @Test
    @DisplayName("Task 4 (Create): GET /products/new displays product-form with backing object")
    void testShowCreateForm() throws Exception {
        mockMvc.perform(get("/products/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("product-form"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    @DisplayName("Task 4 (Create): POST /products with valid data saves and redirects (PRG pattern)")
    void testCreateProductSuccess() throws Exception {
        mockMvc.perform(post("/products")
                        .param("name", "Noise Cancelling Headphones")
                        .param("category", "Audio")
                        .param("price", "199.99")
                        .param("stockQuantity", "30")
                        .param("description", "Premium active noise cancellation headphones"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"))
                .andExpect(flash().attributeExists("successMessage"));
    }

    @Test
    @DisplayName("Task 4 (Validation): POST /products with invalid data redisplays form with BindingResult errors")
    void testCreateProductValidationError() throws Exception {
        mockMvc.perform(post("/products")
                        .param("name", "") // Blank name violates @NotBlank and @Size
                        .param("category", "") // Blank category violates @NotBlank
                        .param("price", "-10.00") // Violates @DecimalMin
                        .param("stockQuantity", "-5")) // Violates @Min
                .andExpect(status().isOk())
                .andExpect(view().name("product-form"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("product", "name"))
                .andExpect(model().attributeHasFieldErrors("product", "category"))
                .andExpect(model().attributeHasFieldErrors("product", "price"))
                .andExpect(model().attributeHasFieldErrors("product", "stockQuantity"));
    }

    @Test
    @DisplayName("Task 4 (Update): GET /products/edit/{id} displays edit form pre-populated")
    void testShowEditFormFound() throws Exception {
        mockMvc.perform(get("/products/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("product-edit"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attribute("product", hasProperty("name", is("Mechanical Keyboard"))));
    }

    @Test
    @DisplayName("Task 4 (Update): GET /products/edit/{id} with invalid ID redirects with flash error")
    void testShowEditFormNotFound() throws Exception {
        mockMvc.perform(get("/products/edit/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"))
                .andExpect(flash().attributeExists("errorMessage"));
    }

    @Test
    @DisplayName("Task 4 (Update): POST /products/edit/{id} with valid data updates and redirects")
    void testUpdateProductSuccess() throws Exception {
        mockMvc.perform(post("/products/edit/1")
                        .param("id", "1")
                        .param("name", "Updated Mechanical Keyboard")
                        .param("category", "Peripherals")
                        .param("price", "149.99")
                        .param("stockQuantity", "50")
                        .param("description", "Updated mechanical keyboard with PBT keycaps"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"))
                .andExpect(flash().attributeExists("successMessage"));
    }

    @Test
    @DisplayName("Task 4 (Update Validation): POST /products/edit/{id} with invalid data redisplays edit form")
    void testUpdateProductValidationError() throws Exception {
        mockMvc.perform(post("/products/edit/1")
                        .param("id", "1")
                        .param("name", "") // Blank name
                        .param("category", "Peripherals")
                        .param("price", "149.99")
                        .param("stockQuantity", "50"))
                .andExpect(status().isOk())
                .andExpect(view().name("product-edit"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("product", "name"));
    }

    @Test
    @DisplayName("Task 4 (Delete): POST /products/delete/{id} removes product and redirects")
    void testDeleteProductSuccess() throws Exception {
        mockMvc.perform(post("/products/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"))
                .andExpect(flash().attributeExists("successMessage"));
    }

    @Test
    @DisplayName("Task 4 (Delete): POST /products/delete/{id} with non-existent ID redirects with error")
    void testDeleteProductNotFound() throws Exception {
        mockMvc.perform(post("/products/delete/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"))
                .andExpect(flash().attributeExists("errorMessage"));
    }
}
