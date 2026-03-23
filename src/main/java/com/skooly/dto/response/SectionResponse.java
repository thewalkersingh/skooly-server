package com.skooly.dto.response;
import com.skooly.model.Section;
import lombok.Data;

@Data
public class SectionResponse {
	private Long id;
	private String name;
	private Integer capacity;
	private Long classId;
	private String className;
	private Long teacherId;
	private String teacherName;
	
	public static SectionResponse from(Section s) {
		SectionResponse r = new SectionResponse();
		r.setId(s.getId());
		r.setName(s.getName());
		r.setCapacity(s.getCapacity());
		if(s.getSchoolClass() != null){
			r.setClassId(s.getSchoolClass().getId());
			r.setClassName(s.getSchoolClass().getName());
		}
		if(s.getTeacher() != null){
			r.setTeacherId(s.getTeacher().getId());
			r.setTeacherName(s.getTeacher().getFirstName()+" "+s.getTeacher().getLastName());
		}
		return r;
	}
}