package com.skooly.dto.request;
import com.skooly.model.Staff;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateStaffRequest {
	@NotNull(message = "User ID is required")
	private Long userId;
	
	@NotBlank(message = "First name is required")
	@Size(max = 100)
	private String firstName;
	
	@NotBlank(message = "Last name is required")
	@Size(max = 100)
	private String lastName;
	private LocalDate dob;
	private Staff.Gender gender;
	private String address;
	
	@Size(max = 20)
	private String phone;
	
	@Email
	private String email;
	private Long departmentId;
	
	@Size(max = 100)
	private String designation;
	private LocalDate joiningDate;
	
	@DecimalMin(value = "0.0")
	private BigDecimal salary;
}