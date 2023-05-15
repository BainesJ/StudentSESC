package uk.ac.leedsbeckett.studentsesc.model;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // The unique student ID
    @Column(unique = true, nullable = true)
    private String studentId;

    // The unique username
    @Column(unique = true, nullable = false)
    private String userName;

    // The password for the student
    @Column(nullable = false, name = "password")
    private String password;

    // The first name of the student
    @Column(nullable = false, name = "first_name")
    private String firstName;

    // The last name of the student
    @Column(nullable = false, name = "last_name")
    private String lastName;

    // Indicates if the student has an outstanding balance
    @Column(nullable = false, name = "outstanding_balance")
    private Boolean outstandingBalance;

    // The set of courses in which the student is enrolled
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "course_student",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    Set<Course> coursesEnrolledIn;

    /**
     * Enrols the student in a course.
     *
     * @param course The course to be enrolled in.
     */
    public void enrolInCourse(Course course) {
        if (coursesEnrolledIn == null) {
            coursesEnrolledIn = new HashSet<>();
        }
        coursesEnrolledIn.add(course);
    }
}