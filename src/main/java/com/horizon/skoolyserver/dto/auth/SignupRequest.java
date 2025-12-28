package com.horizon.skoolyserver.dto.auth;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
public class SignupRequest {
   @NotBlank
   private String username;
   
   @Email
   private String email;
   
   @NotBlank
   private String password;
   
   private Set<String> roles; // e.g., ["admin", "teacher"]
}