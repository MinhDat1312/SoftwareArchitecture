package iuh.fit.pattern01.ui.factory;

import iuh.fit.pattern01.ui.component.Button;
import iuh.fit.pattern01.ui.component.TextBox;

public interface UIFactory {
    Button createButton();
    TextBox createTextBox();
}
