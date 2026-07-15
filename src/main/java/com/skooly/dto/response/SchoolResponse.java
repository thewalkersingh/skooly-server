package com.skooly.dto.response;

import com.skooly.enums.SchoolStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SchoolResponse {
	
	private Long id;
	private String schoolName;
	private String schoolCode;
	private String address;
	private String phone;
	private String email;
	private String logoUrl;
	private SchoolStatus schoolStatus;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
}