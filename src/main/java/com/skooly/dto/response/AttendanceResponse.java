package com.skooly.dto.response;
import com.skooly.model.Attendance;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AttendanceResponse {
	private Long id;
	private LocalDate date;
	private String status;
	private String remarks;
	private Long studentId;
	private String studentName;
	private String studentClass;
	private String studentSection;
	private Long classId;
	private String className;
	
	public static AttendanceResponse from(Attendance a) {
		AttendanceResponse r = new AttendanceResponse();
		r.setId(a.getId());
		r.setDate(a.getDate());
		r.setStatus(a.getStatus().name());
		r.setRemarks(a.getRemarks());
		r.setClassId(a.getSchoolClass().getId());
		r.setClassName(a.getSchoolClass().getName());
		if(a.getStudent() != null){
			r.setStudentId(a.getStudent().getId());
			r.setStudentName(a.getStudent().getFirstName() + " " + a.getStudent().getLastName());
			if(a.getStudent().getSchoolClass() != null)
				r.setStudentClass(a.getStudent().getSchoolClass().getName());
			if(a.getStudent().getSection() != null)
				r.setStudentSection(a.getStudent().getSection().getName());
		}
		return r;
	}
	
}