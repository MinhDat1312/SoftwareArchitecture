package iuh.fit.DesignPattern;

import iuh.fit.DesignPattern.library.domain.Book;
import iuh.fit.DesignPattern.library.service.Library;
import iuh.fit.DesignPattern.library.service.decorator.BasicBorrow;
import iuh.fit.DesignPattern.library.service.decorator.Borrow;
import iuh.fit.DesignPattern.library.service.decorator.ExtendedBorrow;
import iuh.fit.DesignPattern.library.service.decorator.SpecialEditionBorrow;
import iuh.fit.DesignPattern.library.service.factory.BookFactory;
import iuh.fit.DesignPattern.library.service.factory.EBookFactory;
import iuh.fit.DesignPattern.library.service.factory.PaperBookFactory;
import iuh.fit.DesignPattern.library.service.observer.Librarian;
import iuh.fit.DesignPattern.library.service.observer.NotificationService;
import iuh.fit.DesignPattern.library.service.strategy.SearchByTitle;
import iuh.fit.DesignPattern.library.service.strategy.SearchContext;
import iuh.fit.DesignPattern.observer.Task;
import iuh.fit.DesignPattern.observer.TaskStatus;
import iuh.fit.DesignPattern.observer.TeamMember;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class DesignPatternApplication {

	public static void main(String[] args) {
		System.out.println("=== Library Management System with Design Patterns ===");
		// Singleton
		Library library = Library.getInstance();

		// Observer
		NotificationService notificationService = new NotificationService();
		notificationService.subscribe(new Librarian());

		// Factory
		BookFactory paperFactory = new PaperBookFactory();
		BookFactory ebookFactory = new EBookFactory();

		Book b1 = paperFactory.createBook("Java Core", "James", "Programming");
		Book b2 = ebookFactory.createBook("Spring Boot", "Pivotal", "Programming");

		library.addBook(b1);
		library.addBook(b2);

		notificationService.notifyAll("Có sách mới trong thư viện!");

		// Strategy
		SearchContext searchContext = new SearchContext();
		searchContext.setStrategy(new SearchByTitle());

		List<Book> result = searchContext.execute(library.getAllBooks(), "Java");
		System.out.println("🔍 Kết quả tìm kiếm:");
		result.forEach(b -> System.out.println("- " + b.getTitle()));

		// Decorator
		Borrow borrow = new SpecialEditionBorrow(
				new ExtendedBorrow(
						new BasicBorrow()
				)
		);

		System.out.println("📘 Hình thức mượn: " + borrow.borrowInfo());
		System.out.println("=== Library Management System with Design Patterns ===");


		System.out.println("=== Observer Pattern ===");
		Task task = new Task("Implement Observer Pattern");

		TeamMember dev = new TeamMember("Dev A");
		TeamMember tester = new TeamMember("Tester B");

		task.attach(dev);
		task.attach(tester);

		task.setStatus(TaskStatus.IN_PROGRESS);
		task.setStatus(TaskStatus.DONE);
		System.out.println("=== Observer Pattern ===");

	}

}
