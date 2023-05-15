package uk.ac.leedsbeckett.studentsesc.model;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Invoice {
private Long id;
private String reference;
private Double amount;
private LocalDate due;
private String type;
private String status;
private String studentId;
}
