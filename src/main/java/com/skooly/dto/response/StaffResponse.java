package com.skooly.dto.response;

import com.skooly.dto.common.StaffSummary;
import com.skooly.enums.Department;
import com.skooly.enums.StaffRole;
import com.skooly.enums.StaffStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StaffResponse {
	
	private StaffStatus status;
	private Department department;
	private StaffRole staffRole;
	private Long schoolId;
	private String schoolName;
	private StaffSummary staffSummary;
	private LocalDate joiningDate;
	private String photoUrl;
	private AddressResponse address;
	private UserIdentityResponse identity;
	
}