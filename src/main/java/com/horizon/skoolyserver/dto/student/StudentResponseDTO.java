package com.horizon.skoolyserver.dto.student;
import lombok.Data;

@Data
public class StudentResponseDTO {
   private Long id;
   private String admissionNumber;
   private String firstName;
   private String lastName;
   private Long age;
   private String email;
   private String phone;
   private String address;
   private String city;
   private String state;
   private String zip;
   private String country;
   private Long classId;
   private Long parentId;
}