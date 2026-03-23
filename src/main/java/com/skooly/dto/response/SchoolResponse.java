package com.skooly.dto.response;
import com.skooly.model.School;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SchoolResponse {
	private Long id;
	private String name;
	private String code;
	private String address;
	private String phone;
	private String email;
	private String logo;
	private String status;
	private LocalDateTime createdAt;
	
	public static SchoolResponse from(School school) {
		SchoolResponse res = new SchoolResponse();
		res.setId(school.getId());
		res.setName(school.getName());
		res.setCode(school.getCode());
		res.setAddress(school.getAddress());
		res.setPhone(school.getPhone());
		res.setEmail(school.getEmail());
		res.setLogo(school.getLogo());
		res.setStatus(school.getStatus().name());
		res.setCreatedAt(school.getCreatedAt());
		return res;
	}
}
