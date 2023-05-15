package uk.ac.leedsbeckett.studentsesc.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uk.ac.leedsbeckett.studentsesc.model.StudentRepository;
import uk.ac.leedsbeckett.studentsesc.model.Student;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;


@Service
public class StudentService {
    private Student student;

    private List<String> result;
    private StudentRepository studentRepository;
    private RestTemplate restTemplate;
    private IntegrationService integrationService;

    public StudentService(StudentRepository studentRepository, IntegrationService integrationService, RestTemplate restTemplate){
        this.studentRepository = studentRepository;
        this.restTemplate = restTemplate;
        this.integrationService = integrationService;
    }

    public void setCurrentUser(Student stud){
        this.student = studentRepository.findStudentById(stud.getId());
    }

    public void clearCurrentUser(){
        this.student = null;
    }

    public Student getCurrentUser(){
        return this.student;
    }

    public Boolean studentExistsByUserName(String userName){
        return studentRepository.existsStudentByUserName(userName);
    }

    public Boolean studentExistsByStudentId(String studentId){
        return studentRepository.existsStudentByStudentId(studentId);
    }

    public Student getStudentByUserName(String userName){
        return studentRepository.findStudentByUserName(userName);
    }

    public Student getStudentById(Long UID){
        return studentRepository.findStudentById(UID);
    }

    public Student getStudentByStudentId(String StudentId){
        return studentRepository.findStudentByStudentId(StudentId);
    }

    public Boolean studentExistsByUserNameAndPassword(String studentId, String password){
        return studentRepository.existsStudentByUserNameAndPassword(studentId, password);
    }

    public void registerStudent(Student student) {
        if(student == null){
            throw new RuntimeException("ERROR: Missing Critical Data");
        }
        student.setOutstandingBalance(true);
        studentRepository.save(student);
    }

    public Student updateStudent(Student updatedStudent) {
        Class<?> studentClass = Student.class;
        Student student = studentRepository.findStudentByStudentId(updatedStudent.getStudentId());
        Field[] fields = studentClass.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true); // Allow access to private fields
            try {
                Object updatedValue = field.get(updatedStudent); // Get the updated value from the updatedStudent object
                if (updatedValue != null && updatedValue != "") {
                    field.set(student, updatedValue); // Set the updated value to the corresponding field in the student object
                }
            } catch (IllegalAccessException e) {
                // Handle any exceptions that occur during field access
                e.printStackTrace();
            }
        }
        setCurrentUser(student);
        return studentRepository.save(student);
    }

    public List<String> getOutstandingReferences() {
        String id = student.getStudentId();
        try {
            result = integrationService.getOutstandingReferences(id);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Unable to get outstanding references.", e);
        }
    }
}
