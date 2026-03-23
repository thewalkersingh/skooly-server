package com.skooly.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    private LocalDate dob;
    private String gender;
    private String address;
    private String phone;

    @Email(message = "Invalid email format")
    private String email;

    private LocalDate admissionDate;
    private Long classId;
    private Long sectionId;
    private Long parentId;
    private String photo;
    private String status;

    // User account fields
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;
}
