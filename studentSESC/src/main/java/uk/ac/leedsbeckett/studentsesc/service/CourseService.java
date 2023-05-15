package uk.ac.leedsbeckett.studentsesc.service;

import org.springframework.stereotype.Service;
import uk.ac.leedsbeckett.studentsesc.model.Course;
import uk.ac.leedsbeckett.studentsesc.model.CourseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CourseService {

    private CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    /**
     * Gets a list of all courses within the repository.
     * @return - List of all courses.
     */
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    /**
     * Gets a course from its ID.
     * @param Id - The ID to be used as a query.
     * @return - The course corresponding to the ID.
     */
    public Course getCourseByID(Long Id){
        try {
            if (!courseRepository.existsById(Id)) {
                throw new RuntimeException("ERROR: Invalid ID");
            } else {
                return courseRepository.findCourseById(Id);
            }
        } catch (Exception E) {
            throw new RuntimeException("ERROR: There has been an issue with the ID search process. Contact site administrator.");
        }
    }
}
