package com.example.webdev2;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

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
    public List<Book> getAllBooks() {
        return books;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Book getBookById(@PathVariable Long id) {
        return books.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
