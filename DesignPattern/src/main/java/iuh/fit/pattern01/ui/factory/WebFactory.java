package iuh.fit.pattern01.ui.factory;

import iuh.fit.pattern01.ui.component.Button;
import iuh.fit.pattern01.ui.component.TextBox;

public class WebFactory implements UIFactory{
    @Override
    public Button createButton() {
        return new iuh.fit.pattern01.ui.web.Button();
    }

    @Override
    public TextBox createTextBox() {
        return new iuh.fit.pattern01.ui.web.TextBox();
    }
}
