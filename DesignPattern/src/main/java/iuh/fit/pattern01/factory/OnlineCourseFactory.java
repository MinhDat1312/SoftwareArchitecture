package iuh.fit.pattern01.factory;

import iuh.fit.pattern01.model.Course;
import iuh.fit.pattern01.model.OnlineCourse;

public class OnlineCourseFactory extends CourseFactory {
    protected Course createCourse() {
        return new OnlineCourse();
    }
}
