package uk.ac.leedsbeckett.studentsesc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import uk.ac.leedsbeckett.studentsesc.model.Student;
import uk.ac.leedsbeckett.studentsesc.service.EnrolmentService;
import uk.ac.leedsbeckett.studentsesc.service.StudentService;

@RestController
public class StudentController {


    private StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService; //Injecting the studentservice dependency, demonstrating DI and MVC pattern use.
    }

    /**
     * GET mapping for the registration page, allowing a user to register on the service.
     * @return ModelAndView of the registration page, with a student object to fill out.
     */
    @GetMapping(value = "/register")
    public ModelAndView register() {
        ModelAndView modelAndView = new ModelAndView("register");
        modelAndView.addObject("student", new Student());
        return modelAndView;
    }

    /**
     * POST mapping for the registration page.
     * @param student Student object that has been created to be checked against the system.
     * @return Message informing user of success or failure.
     */
    @PostMapping(value = "/register")
    public ModelAndView register(Student student) {
        if(studentService.studentExistsByUserName(student.getUserName())){
            return new ModelAndView("register").addObject("error", "Registration Failed! Username taken.");
        }
        else {
            studentService.registerStudent(student);
            return logIn().addObject("message", "Registration of user " +student.getUserName()+" success!");
        }
    }

    /**
     * GET mapping of the login page, allowing students to log in.
     * @return ModelAndView of the login page, with a student object to enter credentials into.
     */
    @GetMapping(value = "/login")
    public ModelAndView logIn() {
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("student", new Student());
        return modelAndView;
    }

    /**
     * POST mapping of the login page.
     * @param student Created student object to be checked against existing accounts in the database.
     * @return The ModelAndView of a homepage if login is successful, otherwise, the login page with a failure message.
     */
    @PostMapping(value = "/login")
    public ModelAndView checkLogin(Student student) {
        if (studentService.studentExistsByUserName(student.getUserName())) {
            if (studentService.studentExistsByUserNameAndPassword(student.getUserName(), student.getPassword())) {
                Student stud = studentService.getStudentByUserName(student.getUserName());
                studentService.setCurrentUser(stud);
                return homePage();
            }
            else
            {
                return new ModelAndView("login").addObject("error", "Login Failed! Incorrect password.");
            }
        } else return new ModelAndView("login").addObject("error", "Login failed! No user exists under that username. Please check spelling.");
    }

    /**
     * GET mapping of the logout page.
     * @return ModelAndView redirect to the login page, after clearing user session.
     */
    @GetMapping("/logout")
    public ModelAndView logOut() {
        //Resetting current user.
        studentService.clearCurrentUser();
        return new ModelAndView("redirect:/login");
    }

    /**
     * GET mapping for the home page.
     * @return ModelAndView of the home page, with the user's information on it.
     */
    @GetMapping(value = "/home")
    public ModelAndView homePage() {
        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("student", studentService.getCurrentUser());
        return modelAndView;
    }

    /**
     * POST mapping for the home page, allowing users to edit credentials.
     * @param student Student object to edit credentials in the database for.
     * @return Homepage ModelAndView.
     */
    @PostMapping(value = "/home")
    public ModelAndView updateDetails(Student student) {
        studentService.updateStudent(student);
        return homePage();
        }

    /**
     * GET mapping for the mycourses page of a student.
     * @return A ModelAndView page listing all student enrolled courses.
     */
    @GetMapping(value = "/mycourses")
    public ModelAndView myCourses() {
        Student student = studentService.getStudentById(studentService.getCurrentUser().getId());
        if(student.getCoursesEnrolledIn().isEmpty()){
            return new ModelAndView("mycourses");
        }
        return new ModelAndView("mycourses").addObject("courses", student.getCoursesEnrolledIn());
    }

    /**
     * GET mapping for the graduation page, allowing for the user to view all outstanding invoices.
     * @return ModelAndView of the graduation page, with all outstanding references to pay, and an option to graduate if eligible.
     */
    @GetMapping(value = "/graduation")
    public ModelAndView graduation() {
        if (studentService.getCurrentUser().getStudentId() != null && studentService.getCurrentUser().getStudentId() != "")
        {
            return new ModelAndView("graduation").addObject("outstandingReferences", studentService.getOutstandingReferences());
        }
        else
        System.out.println("HELLO");
            return new ModelAndView("/home").addObject(studentService.getCurrentUser()).addObject("error", "You are not currently a student, so you cannot graduate.");
    }

}