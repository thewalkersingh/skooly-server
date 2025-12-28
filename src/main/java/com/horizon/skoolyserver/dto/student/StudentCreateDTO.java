package com.horizon.skoolyserver.dto.student;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StudentCreateDTO {
   @NotBlank
   private String admissionNumber;
   
   @NotBlank
   private String firstName;
   
   @NotBlank
   private String lastName;
   private Long age;
   
   @Email
   private String email;
   private String phone;
   private String address;
   private String city;
   private String state;
   private String zip;
   private String country;
   
   @NotNull
   private Long classId;
   
   @NotNull
   private Long parentId;
}
