package com.skooly.dto.common;

import com.skooly.enums.Department;
import com.skooly.enums.StaffRole;
import com.skooly.enums.StaffStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StaffSummary {
	
	private Long id;
	private String staffName;      // firstName + lastName from identity
	private String phone;
	private Department role;
	private StaffRole staffRole;
	private Department department;
	private StaffStatus staffStatus;
	
}