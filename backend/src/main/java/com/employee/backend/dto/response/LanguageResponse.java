package com.employee.backend.dto.response;
import lombok.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LanguageResponse {
    private Long id;
    private String name;
    private String level;
}