package iuh.fit;

import iuh.fit.pattern01.SystemConfig;
import iuh.fit.pattern01.factory.CourseFactory;
import iuh.fit.pattern01.factory.OnlineCourseFactory;
import iuh.fit.pattern01.ui.component.Button;
import iuh.fit.pattern01.ui.component.TextBox;
import iuh.fit.pattern01.ui.factory.MobileFactory;
import iuh.fit.pattern01.ui.factory.UIFactory;
import iuh.fit.pattern01.ui.factory.WebFactory;

public class Main {
    public static void main(String[] args) {
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
    }
}