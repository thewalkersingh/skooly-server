package com.horizon.skoolyserver.dto.student;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StudentDTO {
	private Long id;
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
	private Long classId;   // reference to SchoolClass
	private Long parentId;  // reference to Parent
}