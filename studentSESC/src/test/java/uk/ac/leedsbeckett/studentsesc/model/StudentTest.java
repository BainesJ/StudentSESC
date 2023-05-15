package uk.ac.leedsbeckett.studentsesc.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student();
    }

    @Test
    void enrolInCourse_ShouldAddCourseToCoursesEnrolledIn() {
        Course course = new Course();
        student.enrolInCourse(course);
        Set<Course> expectedCourses = new HashSet<>();
        expectedCourses.add(course);
        assertEquals(expectedCourses, student.getCoursesEnrolledIn());
    }

    @Test
    void getId_ShouldReturnId() {
        Long id = 1L;
        student.setId(id);
        Long result = student.getId();
        assertEquals(id, result);
    }

    @Test
    void getStudentId_ShouldReturnStudentId() {
        String studentId = "123456";
        student.setStudentId(studentId);
        String result = student.getStudentId();
        assertEquals(studentId, result);
    }

    @Test
    void getUserName_ShouldReturnUserName() {
        String userName = "johnDoe";
        student.setUserName(userName);
        String result = student.getUserName();
        assertEquals(userName, result);
    }

    @Test
    void getPassword_ShouldReturnPassword() {
        String password = "password";
        student.setPassword(password);
        String result = student.getPassword();
        assertEquals(password, result);
    }

    @Test
    void getFirstName_ShouldReturnFirstName() {
        String firstName = "John";
        student.setFirstName(firstName);
        String result = student.getFirstName();
        assertEquals(firstName, result);
    }

    @Test
    void getLastName_ShouldReturnLastName() {
        String lastName = "Doe";
        student.setLastName(lastName);
        String result = student.getLastName();
        assertEquals(lastName, result);
    }

    @Test
    void getOutstandingBalance_ShouldReturnOutstandingBalance() {
        boolean outstandingBalance = true;
        student.setOutstandingBalance(outstandingBalance);
        boolean result = student.getOutstandingBalance();
        assertEquals(outstandingBalance, result);
    }

    @Test
    void getCoursesEnrolledIn_ShouldReturnCoursesEnrolledIn() {
        Set<Course> coursesEnrolledIn = new HashSet<>();
        Course course1 = new Course();
        Course course2 = new Course();
        coursesEnrolledIn.add(course1);
        coursesEnrolledIn.add(course2);
        student.setCoursesEnrolledIn(coursesEnrolledIn);
        Set<Course> result = student.getCoursesEnrolledIn();
        assertEquals(coursesEnrolledIn, result);
    }

    @Test
    void setId_ShouldSetId() {
        Long id = 1L;
        student.setId(id);
        assertEquals(id, student.getId());
    }

    @Test
    void setStudentId_ShouldSetStudentId() {
        String studentId = "123456";
        student.setStudentId(studentId);
        assertEquals(studentId, student.getStudentId());
    }

    @Test
    void setUserName_ShouldSetUserName() {
        String userName = "johnDoe";
        student.setUserName(userName);
        assertEquals(userName, student.getUserName());
    }

    @Test
    void setPassword_ShouldSetPassword() {
        String password = "password";
        student.setPassword(password);
        assertEquals(password, student.getPassword());
    }

    @Test
    void setFirstName_ShouldSetFirstName() {
        String firstName = "John";
        student.setFirstName(firstName);
        assertEquals(firstName, student.getFirstName());
    }
}