package iuh.fit.pattern01.factory;

import iuh.fit.pattern01.model.Course;
import iuh.fit.pattern01.model.OfflineCourse;

public class OfflineCourseFactory extends CourseFactory {
    protected Course createCourse() {
        return new OfflineCourse();
    }
}
