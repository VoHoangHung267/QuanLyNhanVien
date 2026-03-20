package com.employee.backend.dto.response;
import lombok.*;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LoginResponse {
    private String token;
    private String tokenType = "Bearer";
    private String username;
    private String role;
}
