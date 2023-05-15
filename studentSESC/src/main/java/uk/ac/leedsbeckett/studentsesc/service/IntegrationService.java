/**
 * Service class for integrating with external systems.
 */
package uk.ac.leedsbeckett.studentsesc.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uk.ac.leedsbeckett.studentsesc.model.Invoice;
import uk.ac.leedsbeckett.studentsesc.model.Student;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IntegrationService {
    private final RestTemplate restTemplate;

    /**
     * Constructs an IntegrationService instance.
     * @param restTemplate The RestTemplate instance used for sending and receiving RESTful requests.
     */
    public IntegrationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Creates an invoice for a course.
     * @param invoice The invoice to be created.
     * @return The created invoice object.
     */
    public Invoice createCourseInvoice(Invoice invoice) {
        return restTemplate.postForObject("http://localhost:5000/api/invoice/create_invoice", invoice, Invoice.class);
    }

    /**
     * Registers a student in the library system.
     * @param student The student to be registered.
     * @return The response from the registration endpoint.
     */
    public String registerLibraryStudent(Student student) {
        System.out.println("Attempting to create student in library " + student);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("studentId", student.getStudentId());
        params.add("password", student.getPassword());
        return restTemplate.postForObject("http://localhost:8888/api/register", params, String.class);
    }

    /**
     * Retrieves the outstanding invoice references for a student.
     * @param studentId The ID of the student.
     * @return The list of outstanding invoice references.
     * @throws RuntimeException if there is an error retrieving the outstanding invoices.
     */
    public List<String> getOutstandingReferences(String studentId) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("studentId", studentId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<List<String>> responseEntity = restTemplate.exchange(
                    "http://localhost:5000/api/get-outstanding-invoices",
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<List<String>>() {}
            );
            return responseEntity.getBody();
        } catch (HttpClientErrorException ex) {
            throw new RuntimeException("Failed to get outstanding invoices.");
        }
    }
}