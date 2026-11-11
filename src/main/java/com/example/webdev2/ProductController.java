package com.example.webdev2;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller handling product catalog operations, Thymeleaf view rendering,
 * and form data binding with validation.
 */
@Controller
@RequestMapping("/products")
public class ProductController {

    private final List<Product> products = new ArrayList<>();

    public ProductController() {
        resetProducts();
    }

    private void resetProducts() {
        products.clear();
        Category electronics = new Category(1L, "Electronics", "Devices and high-performance gadgetry");
        Category peripherals = new Category(2L, "Peripherals", "Computer accessories and input equipment");
        Category furniture = new Category(3L, "Furniture", "Ergonomic workspace and posture items");

        products.add(new Product(1L, "Mechanical Keyboard", 129.99, peripherals, "Tactile hot-swappable mechanical keyboard with RGB backlighting"));
        products.add(new Product(2L, "UltraWide Monitor", 449.50, electronics, "34-inch curved ultra-wide IPS productivity monitor"));
        products.add(new Product(3L, "Ergonomic Office Chair", 289.00, furniture, "High-back mesh ergonomic desk chair with lumbar support"));
        products.add(new Product(4L, "Wireless Gaming Mouse", 79.99, peripherals, "Low-latency wireless precision mouse with dual sensors"));
    }

    /**
     * Task 1: Product List Template
     * Populates Model with "products" attribute and renders products.html.
     * Supports optional empty=true query parameter to demonstrate th:if empty state.
     */
    @GetMapping
    public String getAllProducts(@RequestParam(name = "empty", required = false, defaultValue = "false") boolean empty,
                                 Model model) {
        if (empty) {
            model.addAttribute("products", new ArrayList<Product>());
        } else {
            model.addAttribute("products", products);
        }
        return "products";
    }

    /**
     * Helper endpoint to reset in-memory products list.
     */
    @GetMapping("/reset")
    public String resetAllProducts() {
        resetProducts();
        return "redirect:/products";
    }

    /**
     * Helper endpoint to clear products to test empty inventory state.
     */
    @GetMapping("/clear")
    public String clearAllProducts() {
        products.clear();
        return "redirect:/products";
    }

    /**
     * Task 2: Product Detail Template
     * Populates Model with "product" attribute and renders product-detail.html.
     * Demonstrates variable expressions and nested property access (product.category.name).
     */
    @GetMapping("/{id}")
    public String getProductById(@PathVariable Long id, Model model) {
        Product product = products.stream()
                .filter(p -> p.getId() != null && p.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (product == null) {
            return "redirect:/products";
        }

        model.addAttribute("product", product);
        return "product-detail";
    }

    /**
     * Task 4: Form View Endpoint
     * Supplies a new backing Product object to the Model for th:object form binding.
     */
    @GetMapping("/new")
    public String showProductForm(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "product-form";
    }

    /**
     * Task 5 Demonstration Endpoint:
     * Simulates form redisplay with binding errors on the price field for visual demonstration.
     */
    @GetMapping("/error-demo")
    public String showProductFormWithError(Model model) {
        Product product = new Product(null, "Faulty Price Device", null, new Category("Electronics"), "Demonstration of field-specific binding error on numeric price");
        org.springframework.validation.BeanPropertyBindingResult result =
                new org.springframework.validation.BeanPropertyBindingResult(product, "product");
        result.rejectValue("price", "typeMismatch",
                "Failed to convert property value of type 'java.lang.String' to required type 'java.lang.Double' for property 'price'; For input string: \"invalid-price-text\"");
        model.addAttribute("product", product);
        model.addAttribute("org.springframework.validation.BindingResult.product", result);
        return "product-form";
    }

    /**
     * Task 4 & Task 5: Handle Product Form Submission with Data Binding & Validation.
     * BindingResult MUST immediately follow @ModelAttribute Product product.
     * If binding errors exist (e.g., non-numeric input for price), redisplays product-form.
     * Otherwise, saves product and redirects to /products.
     */
    @PostMapping
    public String createProduct(@ModelAttribute Product product, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "product-form";
        }

        // Auto-generate ID based on existing max ID
        long maxId = products.stream()
                .mapToLong(p -> p.getId() != null ? p.getId() : 0L)
                .max()
                .orElse(0L);
        product.setId(maxId + 1);

        // Ensure category is non-null for safe nested property access
        if (product.getCategory() == null || product.getCategory().getName() == null || product.getCategory().getName().isBlank()) {
            product.setCategory(new Category(99L, "General", "General merchandise"));
        }

        products.add(product);
        return "redirect:/products";
    }
}
