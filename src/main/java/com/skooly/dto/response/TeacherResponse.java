package com.skooly.dto.response;

import com.skooly.model.Teacher;
import lombok.Data;
import java.time.LocalDate;

@Data
public class TeacherResponse {
	
	private Long id;
	private String firstName;
	private String lastName;
	private String fullName;
	private LocalDate dob;
	private String gender;
	private String address;
	private String phone;
	private String email;
	private LocalDate joiningDate;
	private String qualification;
	private Integer experience;
	private String photo;
	private String status;
	
	private Long subjectId;
	private String subjectName;
	
	private Long userId;
	private String username;
	
	public static TeacherResponse from(Teacher t) {
		TeacherResponse r = new TeacherResponse();
		r.setId(t.getId());
		r.setFirstName(t.getFirstName());
		r.setLastName(t.getLastName());
		r.setFullName(t.getFirstName() + " " + t.getLastName());
		r.setDob(t.getDob());
		r.setGender(t.getGender() != null ? t.getGender().name() : null);
		r.setAddress(t.getAddress());
		r.setPhone(t.getPhone());
		r.setEmail(t.getEmail());
		r.setJoiningDate(t.getJoiningDate());
		r.setQualification(t.getQualification());
		r.setExperience(t.getExperience());
		r.setPhoto(t.getPhoto());
		r.setStatus(t.getStatus().name());
		if (t.getSubject() != null) {
			r.setSubjectId(t.getSubject().getId());
			r.setSubjectName(t.getSubject().getName());
		}
		if (t.getUser() != null) {
			r.setUserId(t.getUser().getId());
			r.setUsername(t.getUser().getUsername());
		}
		return r;
	}
}