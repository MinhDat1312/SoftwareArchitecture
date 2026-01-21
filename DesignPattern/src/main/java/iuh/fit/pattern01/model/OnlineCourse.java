package iuh.fit.pattern01.model;

public class OnlineCourse implements Course{
    @Override
    public void create() {
        System.out.println("Creating an online course");
    }
}
