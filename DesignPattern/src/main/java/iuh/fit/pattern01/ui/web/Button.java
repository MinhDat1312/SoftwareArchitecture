package iuh.fit.pattern01.ui.web;

public class Button implements iuh.fit.pattern01.ui.component.Button {
    @Override
    public void click() {
        System.out.println("Web Button clicked");
    }
}
