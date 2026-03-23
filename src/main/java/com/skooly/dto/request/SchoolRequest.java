package com.skooly.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SchoolRequest {

    @NotBlank(message = "School name is required")
    private String name;

    @NotBlank(message = "School code is required")
    private String code;

    private String address;
    private String phone;

    @Email(message = "Invalid email format")
    private String email;

    private String logo;
}
