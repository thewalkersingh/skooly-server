package com.skooly.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParentSummary {
	
	private Long id;
	private String parentName;    // firstName + lastName from identity
	private String phone;
	private String relation;
	
}