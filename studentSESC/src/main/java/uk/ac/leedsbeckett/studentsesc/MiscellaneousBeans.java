package uk.ac.leedsbeckett.studentsesc;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import uk.ac.leedsbeckett.studentsesc.model.Course;
import uk.ac.leedsbeckett.studentsesc.model.Student;
import uk.ac.leedsbeckett.studentsesc.model.CourseRepository;
import uk.ac.leedsbeckett.studentsesc.model.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class MiscellaneousBeans {

    @Bean
    CommandLineRunner initDatabase(CourseRepository courseRepository, StudentRepository studentRepository) {
        return args -> {
//            Course SESC = new Course();
//            SESC.setTitle("SESC");
//            SESC.setDescription("Software Engineering for Service Computing");
//            SESC.setFee(10.00);
//
//            Course HWHW = new Course();
//            HWHW.setTitle("HWHW");
//            HWHW.setDescription("How to Write Hello World");
//            HWHW.setFee(15.00);
//
//            Course BSB = new Course();
//            BSB.setTitle("BSB");
//            BSB.setDescription("Bogo Sort is the Best");
//            BSB.setFee(20.00);
//            courseRepository.saveAllAndFlush(Set.of(SESC, BSB, HWHW));
//
//            Student student = new Student();
//            student.setStudentId("C9999999");
//            student.setUserName("C9999999");
//            student.setFirstName("C9999999");
//            student.setLastName("C9999999");
//            student.setPassword("password");
//            student.setOutstandingBalance(false);
//            studentRepository.save(student);
        };
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {return builder.build();}
}
