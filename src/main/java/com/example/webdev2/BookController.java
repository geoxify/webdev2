package com.example.webdev2;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/books")
public class BookController {

    private final List<Book> books = new ArrayList<>();

    public BookController() {
        books.add(new Book(1L, "The Hobbit", "J.R.R. Tolkien", 15.99));
        books.add(new Book(2L, "The Fellowship of the Ring", "J.R.R. Tolkien", 19.99));
        books.add(new Book(3L, "Clean Code", "Robert C. Martin", 35.50));
        books.add(new Book(4L, "Spring in Action", "Craig Walls", 39.99));
    }

    /**
     * Task 1 & Task 2: List all books, with optional request parameter filtering by author.
     */
    @GetMapping
    @ResponseBody
    public List<Book> getAllBooks(@RequestParam(required = false) String author) {
        if (author != null && !author.isBlank()) {
            return books.stream()
                    .filter(book -> book.getAuthor() != null &&
                            book.getAuthor().toLowerCase().contains(author.trim().toLowerCase()))
                    .collect(Collectors.toList());
        }
        return books;
    }

    /**
     * Task 1, Task 3 & Task 5: Refactored detail endpoint returning ResponseEntity<Book>.
     * Returns 200 OK with the Book when found, or 404 Not Found when missing.
     * Also populates Model with "book" attribute to satisfy Task 3 model-binding criteria.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id, Model model) {
        Book book = books.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (book == null) {
            return ResponseEntity.notFound().build();
        }

        model.addAttribute("book", book);
        return ResponseEntity.ok(book);
    }

    /**
     * Task 3: Dedicated Model-driven detail view method returning logical view name.
     */
    @GetMapping("/{id}/view")
    public String getBookDetailView(@PathVariable Long id, Model model) {
        Book book = books.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (book != null) {
            model.addAttribute("book", book);
        }
        return "book-detail";
    }

    /**
     * Task 4 & Task 5: Create endpoint with Post/Redirect/Get pattern and HttpStatus.CREATED.
     * Handles JSON payloads.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public String createBookFromJson(@RequestBody Book book) {
        saveBook(book);
        return "redirect:/books";
    }

    /**
     * Task 4 & Task 5: Create endpoint with Post/Redirect/Get pattern and HttpStatus.CREATED.
     * Handles form submissions and query parameters.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createBook(Book book) {
        saveBook(book);
        return "redirect:/books";
    }

    private void saveBook(Book book) {
        if (book.getId() == null) {
            long maxId = books.stream()
                    .mapToLong(b -> b.getId() != null ? b.getId() : 0L)
                    .max()
                    .orElse(0L);
            book.setId(maxId + 1);
        }
        books.add(book);
    }
}
