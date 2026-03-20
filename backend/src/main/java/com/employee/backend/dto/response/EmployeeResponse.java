package com.employee.backend.dto.response;
import lombok.*;
import java.time.LocalDate;
import java.util.List;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EmployeeResponse {
    private Long id;
    private String name;
    private LocalDate dob;
    private String address;
    private String phone;
    private List<LanguageResponse> languages;
    private List<CertificateResponse> certificates;
}
