package com.horizon.skoolyserver.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ParentDTO {
	private Long id;
	
	@NotBlank
	private String name;
	
	@Email
	private String email;
	private String phone;
}