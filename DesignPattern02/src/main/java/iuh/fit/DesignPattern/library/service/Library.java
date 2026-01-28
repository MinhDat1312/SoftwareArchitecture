package iuh.fit.DesignPattern.library.service;

import iuh.fit.DesignPattern.library.domain.Book;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Library {
    private static Library instance;
    private final List<Book> books = new ArrayList<>();

    public static Library getInstance() {
        if (instance == null) {
            instance = new Library();
        }
        return instance;
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public List<Book> getAvailableBooks() {
        return books.stream()
                .filter(Book::isAvailable)
                .toList();
    }

    public List<Book> getAllBooks() {
        return books;
    }
}

