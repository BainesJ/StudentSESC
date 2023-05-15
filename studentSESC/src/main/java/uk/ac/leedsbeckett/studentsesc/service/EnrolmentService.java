package uk.ac.leedsbeckett.studentsesc.service;

import org.springframework.stereotype.Service;
import uk.ac.leedsbeckett.studentsesc.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
public class EnrolmentService {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final StudentService studentService;
    private final IntegrationService integrationService;

    public EnrolmentService(StudentRepository studentRepository, CourseRepository courseRepository, StudentService studentService, IntegrationService integrationService) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.studentService = studentService;
        this.integrationService = integrationService;
    }

    /**
     * Enrols a student in a course.
     *
     * @param course The course to enrol the student in.
     * @return A message indicating the result of the enrolment process.
     */
    public String enrolStudentInCourse(Course course) {
        Student student = getStudent();
        newStudentCheck();
        if (studentEnrolledInCourse(student, course)) {
            return "Student " + getStudent().getStudentId() + " already enrolled in " + course.getTitle();
        } else {
            System.out.println("Student " + student);
            student.enrolInCourse(course);
            studentRepository.save(student);
            Invoice inv = createCourseInvoice(student, course);
            return "Student " + getStudent().getStudentId() + " successfully enrolled in " + course.getTitle() + ". Invoice number: " + inv.getReference();
        }
    }

    /**
     * Checks if a new student needs to be registered and registers them if necessary, generating a student ID and creating an account on the library service.
     */
    public void newStudentCheck() {
        if (getStudent().getCoursesEnrolledIn() == null || getStudent().getCoursesEnrolledIn().isEmpty()) {
            if (getStudent().getStudentId() == null) {
                getStudent().setStudentId(generateID());
                System.out.println("New Student " + getStudent());
                try {
                    integrationService.registerLibraryStudent(getStudent());
                } catch (Exception e) {
                    throw new RuntimeException("Unable to register library student. Maybe the service is down?");
                }
            }
        }
    }

    /**
     * Checks if a student is already enrolled in a course.
     *
     * @param student The student to check.
     * @param course  The course to check.
     * @return true if the student is enrolled in the course, false otherwise.
     */
    public boolean studentEnrolledInCourse(Student student, Course course) {
        Set<Course> enrolledCourses = student.getCoursesEnrolledIn();
        for (Course c : enrolledCourses) {
            if (c.getId().equals(course.getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Generates a unique student ID.
     *
     * @return The generated student ID.
     */
    public String generateID() {
        String studentId;
        while (true) {
            studentId = "C";
            Random r = new Random();
            for (int i = 0; i < 7; i++) {
                studentId = studentId.concat(String.valueOf(r.nextInt(10)));
            }
            if (!studentService.studentExistsByStudentId(studentId)) {
                return studentId;
            }
        }
    }

    /**
     * Gets the current student.
     *
     * @return The current student.
     */
    public Student getStudent() {
        return studentService.getCurrentUser();
    }

    /**
     * Searches for matching courses based on a query.
     *
     * @param query The title query to be used for searching courses.
     * @return A list of courses matching the title query.
     */
    public List<Course> queryCourses(String query) {
        List<Course> courses = courseRepository.findAll();
        List<Course> matches = new ArrayList<>();
        for (Course c : courses) {
            if (c.getTitle().toUpperCase().contains(query.toUpperCase())) {
                matches.add(c);
            }
        }
        return matches;
    }

    /**
     * Creates a course invoice for a student.
     *
     * @param student The student associated with the invoice.
     * @param course  The course associated with the invoice.
     * @return The created invoice.
     */
    public Invoice createCourseInvoice(Student student, Course course) {
        Invoice invoice = new Invoice();
        invoice.setAmount(course.getFee());
        invoice.setDue(LocalDate.now().plusYears(1));
        invoice.setType("COURSE_FEE");
        invoice.setStudentId(student.getStudentId());
        return integrationService.createCourseInvoice(invoice);
    }
}