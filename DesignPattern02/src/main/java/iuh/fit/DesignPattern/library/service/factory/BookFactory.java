package iuh.fit.DesignPattern.library.service.factory;

import iuh.fit.DesignPattern.library.domain.Book;

public interface BookFactory {
    Book createBook(String title, String author, String category);
}

