package uk.ac.leedsbeckett.studentsesc.controller;


import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.leedsbeckett.studentsesc.model.Course;
import uk.ac.leedsbeckett.studentsesc.service.CourseService;
import uk.ac.leedsbeckett.studentsesc.service.EnrolmentService;

import java.util.List;

@RestController
public class CourseController {
    private EnrolmentService enrolmentService;
    private CourseService courseService;

    public CourseController(CourseService courseService, EnrolmentService enrolmentService) {
        this.courseService = courseService;
        this.enrolmentService = enrolmentService; //Constructor method demonstrating DI and MVC design pattern.
    }

    /**
     * GET mapping to display a list of all courses.
     * @return ModelAndView of a page with all courses.
     */
    @GetMapping("/courses")
    public ModelAndView viewAllCourses() {
        List<Course> courseList = courseService.getAllCourses();
        return new ModelAndView("courses").addObject("courses", courseList);
    }

    /**
     * POST mapping to use the search bar to subsearch the courses page.
     * @param query Query to check against course titles.
     * @return Updated ModelAndView with matched courses.
     */
    @PostMapping("/courses")
    public ModelAndView searchCoursesPost(@RequestParam("query") String query) {
        List<Course> matches = enrolmentService.queryCourses(query);
        return new ModelAndView("courses").addObject("courses", matches);
    }

    /**
     * GET mapping to view a specific course's information.
     * @param id Unique ID of the course to view.
     * @return ModelAndView of the course page, with enrolment status that toggles a button that allows enrolment.
     */
    @GetMapping("/course/{id}")
    public ModelAndView viewCourse(@PathVariable Long id) {
        Course course = courseService.getCourseByID(id);
        ModelAndView modelAndView = new ModelAndView("course");
        return modelAndView.addObject("course",course).addObject("enrolled", enrolmentService.studentEnrolledInCourse(enrolmentService.getStudent(), course));
    }

    /**
     *POST mapping to use the enrol button on the course ID page.
     * @param id Unique ID of the course to view.
     * @return ModelAndView of the mycourses page, with a message stating success or failure in enrolment.
     */
    @PostMapping("/course/{id}")
    public ModelAndView EnrolCourse(@PathVariable Long id) {
        String message = enrolmentService.enrolStudentInCourse(courseService.getCourseByID(id)).toString();
        return new ModelAndView("redirect:/mycourses").addObject("message", message);
    }
}
