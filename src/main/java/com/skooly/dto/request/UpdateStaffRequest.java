package com.skooly.dto.request;
import com.skooly.model.Staff;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateStaffRequest {
	@Size(max = 100)
	private String firstName;
	
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