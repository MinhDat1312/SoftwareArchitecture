package iuh.fit.DesignPattern.library.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Book {
    protected String title;
    protected String author;
    protected String category;
    protected boolean available = true;

    public void borrow() {
        this.available = false;
    }

    public void giveBack() {
        this.available = true;
    }
}
