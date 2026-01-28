package iuh.fit.DesignPattern.library.service.factory;

import iuh.fit.DesignPattern.library.domain.AudioBook;
import iuh.fit.DesignPattern.library.domain.Book;
import org.springframework.stereotype.Component;

@Component("AudioBookFactory")
public class AudioBookFactory implements BookFactory {
    @Override
    public Book createBook(String title, String author, String category) {
        AudioBook book = new AudioBook();
        book.setTitle(title);
        book.setAuthor(author);
        book.setCategory(category);
        return book;
    }
}
