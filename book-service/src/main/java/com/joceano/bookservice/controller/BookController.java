package com.joceano.bookservice.controller;

import com.joceano.bookservice.model.Book;
import com.joceano.bookservice.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("book-service")
public class BookController {

    @Autowired
    private Environment environment;

    @Autowired
    private BookRepository repository;

    @GetMapping("/{id}/{currency}")
    public Book findBook(@PathVariable("id") Long id, @PathVariable("currency") String currency) {
        var book = repository.getById(id);
        if (Objects.isNull(book))
            throw new RuntimeException("Book not found");
        var port = environment.getProperty("local.server.port");
        book.setEnvironment(port);
        return book;
    }
}
