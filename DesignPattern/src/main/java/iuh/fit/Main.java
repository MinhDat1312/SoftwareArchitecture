package iuh.fit;

import iuh.fit.pattern01.SystemConfig;
import iuh.fit.pattern01.factory.CourseFactory;
import iuh.fit.pattern01.factory.OnlineCourseFactory;
import iuh.fit.pattern01.ui.component.Button;
import iuh.fit.pattern01.ui.component.TextBox;
import iuh.fit.pattern01.ui.factory.MobileFactory;
import iuh.fit.pattern01.ui.factory.UIFactory;
import iuh.fit.pattern01.ui.factory.WebFactory;
import iuh.fit.pattern02.decorator.CreditCardPayment;
import iuh.fit.pattern02.decorator.DiscountDecorator;
import iuh.fit.pattern02.decorator.Payment;
import iuh.fit.pattern02.decorator.ProcessingFeeDecorator;
import iuh.fit.pattern02.model.Order;
import iuh.fit.pattern02.strategy.TaxStrategy;
import iuh.fit.pattern02.strategy.VATTax;

public class Main {
    public static void main(String[] args) {

        System.out.println("=====================================Singleton, Factory Method and Abstract Factory Pattern Demo=====================================");
        SystemConfig config = SystemConfig.getInstance();
        System.out.println(config.getSystemName());

        UIFactory uiFactory;
        if (config.getPlatform().equals("WEB")) {
            uiFactory = new WebFactory();
        } else {
            uiFactory = new MobileFactory();
        }

        Button button = uiFactory.createButton();
        TextBox textBox = uiFactory.createTextBox();
        button.click();
        textBox.render();

        CourseFactory courseFactory = new OnlineCourseFactory();
        courseFactory.registerCourse();
        System.out.println("=====================================Singleton, Factory Method and Abstract Factory Pattern Demo=====================================");


        System.out.println("=====================================State, Strategy, Decorator Pattern Demo=====================================");
        TaxStrategy taxStrategy = new VATTax();

        Payment payment = new DiscountDecorator(
                new ProcessingFeeDecorator(
                        new CreditCardPayment()
                )
        );

        Order order = new Order(1000, taxStrategy, payment);
        order.nextState();
        order.nextState();

        order.checkout();
        System.out.println("=====================================State, Strategy, Decorator Pattern Demo=====================================");
    }
}