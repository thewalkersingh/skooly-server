package com.skooly.dto.response;
import com.skooly.model.SchoolClass;
import lombok.Data;

@Data
public class SchoolClassResponse {
	private Long id;
	private String name;
	private Integer gradeLevel;
	
	public static SchoolClassResponse from(SchoolClass c) {
		SchoolClassResponse r = new SchoolClassResponse();
		r.setId(c.getId());
		r.setName(c.getName());
		r.setGradeLevel(c.getGradeLevel());
		return r;
	}
}