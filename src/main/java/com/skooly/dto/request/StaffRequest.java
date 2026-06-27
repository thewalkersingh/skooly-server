package com.skooly.dto.request;

import com.skooly.enums.Department;
import com.skooly.enums.StaffRole;
import com.skooly.enums.StaffStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StaffRequest {
	
	@NotNull
	private StaffStatus status;
	
	private Department department;     // role/dept is required
	private StaffRole staffRole;
	
	@Size(max = 200)
	private String qualification;           // nullable
	
	private Integer experience;             // nullable
	
	private LocalDate joiningDate;
	private LocalDate dob;
	
	@Size(max = 500)
	private String photoUrl;
	
	private AddressRequest address;
	
	@NotNull
	private UserIdentityRequest identity;   // required
	
}