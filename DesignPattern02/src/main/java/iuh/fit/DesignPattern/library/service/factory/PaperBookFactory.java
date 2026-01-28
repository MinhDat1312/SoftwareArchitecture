package iuh.fit.DesignPattern.library.service.factory;

import iuh.fit.DesignPattern.library.domain.Book;
import iuh.fit.DesignPattern.library.domain.PaperBook;
import org.springframework.stereotype.Component;

@Component("paperBookFactory")
public class PaperBookFactory implements BookFactory {
    @Override
    public Book createBook(String title, String author, String category) {
        PaperBook book = new PaperBook();
        book.setTitle(title);
        book.setAuthor(author);
        book.setCategory(category);
        return book;
    }
}

