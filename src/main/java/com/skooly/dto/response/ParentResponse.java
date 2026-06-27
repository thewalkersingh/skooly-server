package com.skooly.dto.response;

import com.skooly.dto.common.StudentSummary;
import com.skooly.enums.ParentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParentResponse {
	
	private Long id;
	private String occupation;
	private String relation;
	private ParentStatus status;
	private AddressResponse address;   // embedded DTO
	private UserIdentityResponse identity;  // nested DTO
	private List<StudentSummary> students; // nested list of children
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
}