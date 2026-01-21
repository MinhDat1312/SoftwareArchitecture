package iuh.fit.pattern01.factory;

import iuh.fit.pattern01.model.Course;

public abstract class CourseFactory {

    public void registerCourse() {
        Course course = createCourse();
        course.create();
    }

    protected abstract Course createCourse();
}
