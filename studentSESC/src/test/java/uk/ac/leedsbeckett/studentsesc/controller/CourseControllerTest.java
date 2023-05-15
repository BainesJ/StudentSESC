package uk.ac.leedsbeckett.studentsesc.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.leedsbeckett.studentsesc.model.Course;
import uk.ac.leedsbeckett.studentsesc.service.CourseService;
import uk.ac.leedsbeckett.studentsesc.service.EnrolmentService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class CourseControllerTest {

    @Mock
    private CourseService courseService;

    @Mock
    private EnrolmentService enrolmentService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void viewAllCourses() {
        CourseController courseController = new CourseController(courseService, enrolmentService);

        List<Course> courses = new ArrayList<>();
        Course course1 = new Course();
        course1.setId(1L);
        course1.setTitle("Course 1");
        Course course2 = new Course();
        course2.setId(2L);
        course2.setTitle("Course 2");
        courses.add(course1);
        courses.add(course2);

        when(courseService.getAllCourses()).thenReturn(courses);

        ModelAndView modelAndView = courseController.viewAllCourses();

        assertEquals("courses", modelAndView.getViewName());
        assertEquals(courses, modelAndView.getModel().get("courses"));
    }

    @Test
    void searchCoursesPost() {
        CourseController courseController = new CourseController(courseService, enrolmentService);

        List<Course> matches = new ArrayList<>();
        Course course = new Course();
        course.setId(1L);
        course.setTitle("Course 1");
        matches.add(course);

        String query = "Course 1";

        when(enrolmentService.queryCourses(query)).thenReturn(matches);

        ModelAndView modelAndView = courseController.searchCoursesPost(query);

        assertEquals("courses", modelAndView.getViewName());
        assertEquals(matches, modelAndView.getModel().get("courses"));
    }

    @Test
    void viewCourse() {
        CourseController courseController = new CourseController(courseService, enrolmentService);

        Long courseId = 1L;

        Course course = new Course();
        course.setId(courseId);
        course.setTitle("Course 1");

        when(courseService.getCourseByID(courseId)).thenReturn(course);
        when(enrolmentService.studentEnrolledInCourse(enrolmentService.getStudent(), course)).thenReturn(true);

        ModelAndView modelAndView = courseController.viewCourse(courseId);

        assertEquals("course", modelAndView.getViewName());
        assertEquals(course, modelAndView.getModel().get("course"));
        assertTrue((boolean) modelAndView.getModel().get("enrolled"));
    }

    @Test
    void enrolCourse() {
        CourseController courseController = new CourseController(courseService, enrolmentService);

        Long courseId = 1L;

        Course course = new Course();
        course.setId(courseId);
        course.setTitle("Course 1");

        when(courseService.getCourseByID(courseId)).thenReturn(course);
        when(enrolmentService.enrolStudentInCourse(course)).thenReturn("Success");

        ModelAndView modelAndView = courseController.EnrolCourse(courseId);

        assertEquals("redirect:/mycourses", modelAndView.getViewName());
        assertEquals("Success", modelAndView.getModel().get("message"));
    }
}
