package uk.ac.leedsbeckett.studentsesc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.Set;

@Entity
@Data
public class Course {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    // The title of the course
    @Column(nullable = false, name = "title")
    private String title;

    // The description of the course
    @Column(nullable = false, name = "description")
    private String description;

    // The fee associated with the course
    @Column(nullable = false, name = "fee")
    private Double fee;

    // The set of students enrolled in the course
    @ManyToMany(mappedBy = "coursesEnrolledIn")
    @JsonIgnore
    @ToString.Exclude
    Set<Student> studentsEnrolledInCourse;
}