package iuh.fit.DesignPattern.library.service.factory;

import iuh.fit.DesignPattern.library.domain.Book;
import iuh.fit.DesignPattern.library.domain.EBook;
import org.springframework.stereotype.Component;

@Component("EBookFactory")
public class EBookFactory implements BookFactory {
    @Override
    public Book createBook(String title, String author, String category) {
        EBook book = new EBook();
        book.setTitle(title);
        book.setAuthor(author);
        book.setCategory(category);
        return book;
    }
}
