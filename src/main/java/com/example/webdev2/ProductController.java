package com.example.webdev2;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Controller handling product catalog operations, Thymeleaf view rendering,
 * and form data binding with Jakarta Bean Validation.
 * Follows the Controller-Service-Repository pattern by delegating business logic to ProductService.
 */
@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Task 4: List View (Read)
     * Displays all current items in a formatted table with links to add, edit, or delete.
     * Supports optional empty=true query parameter to demonstrate empty state handling.
     */
    @GetMapping
    public String getAllProducts(@RequestParam(name = "empty", required = false, defaultValue = "false") boolean empty,
                                 Model model) {
        if (empty) {
            model.addAttribute("products", new ArrayList<Product>());
        } else {
            model.addAttribute("products", productService.getAllProducts());
        }
        return "products";
    }

    /**
     * Read: Product Detail View
     * Displays complete product specifications for a given ID.
     */
    @GetMapping("/{id}")
    public String getProductById(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Product> productOpt = productService.getProductById(id);
        if (productOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Product with ID #" + id + " was not found.");
            return "redirect:/products";
        }
        model.addAttribute("product", productOpt.get());
        return "product-detail";
    }

    /**
     * Task 4: Create Form (Create)
     * Renders empty product form with backing Product object for th:object binding.
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        if (!model.containsAttribute("product")) {
            model.addAttribute("product", new Product());
        }
        return "product-form";
    }

    /**
     * Task 4: Create Action (Create)
     * Handles product creation form submission with Jakarta Bean Validation.
     * BindingResult must immediately follow the validated model attribute.
     */
    @PostMapping
    public String createProduct(@Valid @ModelAttribute("product") Product product,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "product-form";
        }
        Product created = productService.createProduct(product);
        redirectAttributes.addFlashAttribute("successMessage",
                "Product '" + created.getName() + "' (ID #" + created.getId() + ") was created successfully!");
        return "redirect:/products";
    }

    /**
     * Task 4: Edit Form (Update)
     * Renders pre-populated form allowing users to modify an existing item's details.
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Product> productOpt = productService.getProductById(id);
        if (productOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Cannot edit: Product with ID #" + id + " was not found.");
            return "redirect:/products";
        }
        model.addAttribute("product", productOpt.get());
        return "product-edit";
    }

    /**
     * Task 4: Update Action (Update)
     * Handles update form submission with validation.
     * If validation fails, redisplays product-edit with field error messages.
     */
    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable Long id,
                                @Valid @ModelAttribute("product") Product product,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            product.setId(id);
            return "product-edit";
        }
        try {
            productService.updateProduct(id, product);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Product #" + id + " ('" + product.getName() + "') was updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/products";
    }

    /**
     * Task 4: Delete Action (Delete)
     * Removes product by ID and redirects back to product list with status message.
     */
    @PostMapping("/delete/{id}")
    public String deleteProductPost(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean deleted = productService.deleteProduct(id);
        if (deleted) {
            redirectAttributes.addFlashAttribute("successMessage",
                    "Product #" + id + " was deleted successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Could not delete: Product #" + id + " does not exist.");
        }
        return "redirect:/products";
    }

    /**
     * GET fallback for delete action (e.g. direct link or query navigation).
     */
    @GetMapping("/delete/{id}")
    public String deleteProductGet(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        return deleteProductPost(id, redirectAttributes);
    }

    /**
     * Helper endpoint to reset catalog back to demo data.
     */
    @GetMapping("/reset")
    public String resetAllProducts(RedirectAttributes redirectAttributes) {
        productService.resetCatalog();
        redirectAttributes.addFlashAttribute("infoMessage",
                "Product catalog has been reset to default inventory data.");
        return "redirect:/products";
    }

    /**
     * Helper endpoint to clear all products for empty-state testing.
     */
    @GetMapping("/clear")
    public String clearAllProducts(RedirectAttributes redirectAttributes) {
        productService.clearCatalog();
        redirectAttributes.addFlashAttribute("infoMessage",
                "All products have been cleared. Inventory is now empty.");
        return "redirect:/products";
    }
}
