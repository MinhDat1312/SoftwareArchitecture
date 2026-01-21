package iuh.fit.pattern01.model;

public class OfflineCourse implements Course {
    @Override
    public void create() {
        System.out.println("Creating an offline course");
    }
}
