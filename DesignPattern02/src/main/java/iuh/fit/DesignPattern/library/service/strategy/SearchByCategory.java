package iuh.fit.DesignPattern.library.service.strategy;

import iuh.fit.DesignPattern.library.domain.Book;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SearchByCategory implements SearchStrategy{
    @Override
    public List<Book> search(List<Book> books, String keyword) {
        return books.stream()
                .filter(b -> b.getCategory().equalsIgnoreCase(keyword))
                .toList();
    }
}
