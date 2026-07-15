package com.skooly.dto.request;

import com.skooly.enums.SchoolStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SchoolRequest {
	
	@NotNull
	@Size(max = 100)
	private String schoolName;
	
	@Size(max = 20)
	private String schoolCode;
	private String address;
	
	@NotNull
	private SchoolStatus schoolStatus;
	
	@Size(max = 15)
	private String phone;
	
	@Email
	private String email;
	private String logoUrl;
	
}