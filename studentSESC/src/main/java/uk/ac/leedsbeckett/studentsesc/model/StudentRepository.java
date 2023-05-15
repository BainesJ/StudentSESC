package uk.ac.leedsbeckett.studentsesc.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>{
    Student findStudentById(Long UID);
    Student findStudentByStudentId(String studentId);
    Student findStudentByUserName(String userName);
    Boolean existsStudentByStudentId(String studentId);
    Boolean existsStudentByUserName(String userName);
    Boolean existsStudentByUserNameAndPassword(String userName, String password);
}