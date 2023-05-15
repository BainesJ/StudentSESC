package uk.ac.leedsbeckett.studentsesc.service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.ac.leedsbeckett.studentsesc.model.Course;
import uk.ac.leedsbeckett.studentsesc.model.CourseRepository;
import uk.ac.leedsbeckett.studentsesc.service.CourseService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceTest {
    @Mock
    private CourseRepository courseRepository;

    private CourseService courseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        courseService = new CourseService(courseRepository);
    }

    @Test
    void getAllCourses_ReturnsListOfCourses() {
        List<Course> courses = new ArrayList<>();
        Course course1 = new Course();
        course1.setId(1L);
        course1.setTitle("Course 1");
        courses.add(course1);

        Course course2 = new Course();
        course2.setId(2L);
        course2.setTitle("Course 2");
        courses.add(course2);

        when(courseRepository.findAll()).thenReturn(courses);

        assertEquals(courses, courseService.getAllCourses());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void getCourseByID_ExistingID_ReturnsCourse() {
        long courseId = 1L;
        Course course = new Course();
        course.setId(courseId);
        course.setTitle("Course 1");

        when(courseRepository.existsById(courseId)).thenReturn(true);
        when(courseRepository.findCourseById(courseId)).thenReturn(course);

        Course result = courseService.getCourseByID(courseId);

        assertEquals(course, result);
        verify(courseRepository, times(1)).existsById(courseId);
        verify(courseRepository, times(1)).findCourseById(courseId);
    }

    @Test
    void getCourseByID_NonExistingID_ThrowsException() {
        long nonExistingID = 100L;

        when(courseRepository.existsById(nonExistingID)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> courseService.getCourseByID(nonExistingID));
        verify(courseRepository, times(1)).existsById(nonExistingID);
        verify(courseRepository, never()).findCourseById(nonExistingID);
    }
}