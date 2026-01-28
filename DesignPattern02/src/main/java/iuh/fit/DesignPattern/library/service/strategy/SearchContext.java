package iuh.fit.DesignPattern.library.service.strategy;

import iuh.fit.DesignPattern.library.domain.Book;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchContext {

    private SearchStrategy strategy;

    public void setStrategy(SearchStrategy strategy) {
        this.strategy = strategy;
    }

    public List<Book> execute(List<Book> books, String keyword) {
        return strategy.search(books, keyword);
    }
}
