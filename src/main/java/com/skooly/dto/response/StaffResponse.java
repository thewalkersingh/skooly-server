package com.skooly.dto.response;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffResponse {
	private Long id;
	private Long userId;
	private String firstName;
	private String lastName;
	private LocalDate dob;
	private String gender;
	private String address;
	private String phone;
	private String email;
	private Long departmentId;
	private String departmentName;
	private String designation;
	private LocalDate joiningDate;
	private BigDecimal salary;
	private String photo;
	private String status;
	private LocalDateTime createdAt;
}