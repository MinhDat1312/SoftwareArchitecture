package iuh.fit.DesignPattern.library.service.strategy;

import iuh.fit.DesignPattern.library.domain.Book;

import java.util.List;

public interface SearchStrategy {
    List<Book> search(List<Book> books, String keyword);
}
