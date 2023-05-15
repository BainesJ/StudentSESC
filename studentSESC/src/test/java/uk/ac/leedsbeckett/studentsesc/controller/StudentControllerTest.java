package uk.ac.leedsbeckett.studentsesc.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import uk.ac.leedsbeckett.studentsesc.model.Student;
import uk.ac.leedsbeckett.studentsesc.service.StudentService;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_ShouldReturnRegisterPageWithStudentObject() {
        ModelAndView expectedModelAndView = new ModelAndView("register");
        expectedModelAndView.addObject("student", new Student());

        ModelAndView modelAndView = studentController.register();

        assertEquals(expectedModelAndView.getViewName(), modelAndView.getViewName());
        assertEquals(expectedModelAndView.getModel(), modelAndView.getModel());
    }

    @Test
    void register_ShouldReturnRegisterPageWithError_WhenUsernameIsTaken() {
        Student student = new Student();
        student.setUserName("existingUser");

        when(studentService.studentExistsByUserName("existingUser")).thenReturn(true);

        ModelAndView modelAndView = studentController.register(student);

        assertEquals("register", modelAndView.getViewName());
        assertTrue(modelAndView.getModel().containsKey("error"));
        assertEquals("Registration Failed! Username taken.", modelAndView.getModel().get("error"));
        verify(studentService, never()).registerStudent(student);
    }

    @Test
    void register_ShouldReturnLoginWithSuccessMessage_WhenRegistrationIsSuccessful() {
        Student student = new Student();
        student.setUserName("newUser");

        when(studentService.studentExistsByUserName("newUser")).thenReturn(false);

        ModelAndView modelAndView = studentController.register(student);

        assertEquals("login", modelAndView.getViewName());
        assertTrue(modelAndView.getModel().containsKey("message"));
        assertEquals("Registration of user newUser success!", modelAndView.getModel().get("message"));
        verify(studentService).registerStudent(student);
    }

    @Test
    void logIn_ShouldReturnLoginPageWithStudentObject() {
        ModelAndView expectedModelAndView = new ModelAndView("login");
        expectedModelAndView.addObject("student", new Student());

        ModelAndView modelAndView = studentController.logIn();

        assertEquals(expectedModelAndView.getViewName(), modelAndView.getViewName());
        assertEquals(expectedModelAndView.getModel(), modelAndView.getModel());
    }

    @Test
    void checkLogin_ShouldReturnHomePage_WhenLoginIsSuccessful() {
        Student student = new Student();
        student.setUserName("existingUser");
        student.setPassword("password");

        when(studentService.studentExistsByUserName("existingUser")).thenReturn(true);
        when(studentService.studentExistsByUserNameAndPassword("existingUser", "password")).thenReturn(true);
        when(studentService.getStudentByUserName("existingUser")).thenReturn(student);

        ModelAndView modelAndView = studentController.checkLogin(student);

        assertEquals("home", modelAndView.getViewName());
        assertNull(modelAndView.getModel().get("error"));
        verify(studentService).setCurrentUser(student);
    }

    @Test
    void checkLogin_ShouldReturnLoginPageWithError_WhenIncorrectPasswordIsProvided() {
        Student student = new Student();
        student.setUserName("existingUser");
        student.setPassword("wrongPassword");

        when(studentService.studentExistsByUserName("existingUser")).thenReturn(true);
        when(studentService.studentExistsByUserNameAndPassword("existingUser", "wrongPassword")).thenReturn(false);

        ModelAndView modelAndView = studentController.checkLogin(student);

        assertEquals("login", modelAndView.getViewName());
        assertTrue(modelAndView.getModel().containsKey("error"));
        assertEquals("Login Failed! Incorrect password.", modelAndView.getModel().get("error"));
    }

    @Test
    void checkLogin_ShouldReturnLoginPageWithError_WhenUserDoesNotExist() {
        Student student = new Student();
        student.setUserName("nonExistingUser");

        when(studentService.studentExistsByUserName("nonExistingUser")).thenReturn(false);

        ModelAndView modelAndView = studentController.checkLogin(student);

        assertEquals("login", modelAndView.getViewName());
        assertTrue(modelAndView.getModel().containsKey("error"));
        assertEquals("Login failed! No user exists under that username. Please check spelling.", modelAndView.getModel().get("error"));
    }

    @Test
    void logOut_ShouldClearCurrentUserAndRedirectToLoginPage() {
        RedirectView expectedModelAndView = new RedirectView("/login");

        ModelAndView modelAndView = studentController.logOut();

        assertEquals("redirect:"+expectedModelAndView.getUrl(), modelAndView.getViewName());
        verify(studentService).clearCurrentUser();
    }

    @Test
    void homePage_ShouldReturnHomePageWithCurrentUserInformation() {
        Student currentUser = new Student();
        currentUser.setId(1L);
        currentUser.setUserName("currentUser");

        when(studentService.getCurrentUser()).thenReturn(currentUser);

        ModelAndView expectedModelAndView = new ModelAndView("home");
        expectedModelAndView.addObject("student", currentUser);

        ModelAndView modelAndView = studentController.homePage();

        assertEquals(expectedModelAndView.getViewName(), modelAndView.getViewName());
        assertEquals(expectedModelAndView.getModel(), modelAndView.getModel());
        verify(studentService).getCurrentUser();
    }

    @Test
    void updateDetails_ShouldReturnHomePageAfterUpdatingStudentDetails() {
        Student student = new Student();
        student.setId(1L);
        student.setUserName("currentUser");

        ModelAndView expectedModelAndView = new ModelAndView("home");
        expectedModelAndView.addObject("student", null);

        ModelAndView modelAndView = studentController.updateDetails(student);

        assertEquals(expectedModelAndView.getViewName(), modelAndView.getViewName());
        assertEquals(expectedModelAndView.getModel(), modelAndView.getModel());
        verify(studentService).updateStudent(student);
    }

    @Test
    void myCourses_ShouldReturnMyCoursesPageWithEmptyCourseList_WhenStudentIsNotEnrolledInAnyCourses() {
        StudentService studentService = Mockito.mock(StudentService.class);

        StudentController studentController = new StudentController(studentService);

        Student currentUser = Mockito.mock(Student.class);
        when(studentService.getCurrentUser()).thenReturn(currentUser);
        when(currentUser.getId()).thenReturn(1L);
        when(studentService.getStudentById(1L)).thenReturn(currentUser);
        when(currentUser.getCoursesEnrolledIn()).thenReturn(Collections.emptySet());

        ModelAndView expectedModelAndView = new ModelAndView("mycourses");

        ModelAndView modelAndView = studentController.myCourses();

        assertEquals(expectedModelAndView.getViewName(), modelAndView.getViewName());
        assertEquals(expectedModelAndView.getModel(), modelAndView.getModel());
        verify(studentService).getCurrentUser();
        verify(studentService).getStudentById(1L);
        verify(currentUser).getCoursesEnrolledIn();
    }

}
       
