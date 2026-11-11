package com.example.webdev2;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @GetMapping("/{id}")
    public String getBookById(@PathVariable Long id, Model model) {
        Book book = books.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElse(null);
        model.addAttribute("book", book);
        return "book-detail";
    }
}
