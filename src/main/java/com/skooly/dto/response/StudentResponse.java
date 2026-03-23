package com.skooly.dto.response;
import com.skooly.model.Student;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentResponse {
	private Long id;
	private String firstName;
	private String lastName;
	private String fullName;
	private LocalDate dob;
	private String gender;
	private String address;
	private String phone;
	private String email;
	private LocalDate admissionDate;
	private String status;
	private String photo;
	// Class & section
	private Long classId;
	private String className;
	private Long sectionId;
	private String sectionName;
	// Parent
	private Long parentId;
	private String parentName;
	// User account
	private Long userId;
	private String username;
	
	public static StudentResponse from(Student s) {
		StudentResponse r = new StudentResponse();
		r.setId(s.getId());
		r.setFirstName(s.getFirstName());
		r.setLastName(s.getLastName());
		r.setFullName(s.getFirstName()+" "+s.getLastName());
		r.setDob(s.getDob());
		r.setGender(s.getGender() != null ? s.getGender().name() : null);
		r.setAddress(s.getAddress());
		r.setPhone(s.getPhone());
		r.setEmail(s.getEmail());
		r.setAdmissionDate(s.getAdmissionDate());
		r.setStatus(s.getStatus().name());
		r.setPhoto(s.getPhoto());
		
		if(s.getSchoolClass() != null){
			r.setClassId(s.getSchoolClass().getId());
			r.setClassName(s.getSchoolClass().getName());
		}
		if(s.getSection() != null){
			r.setSectionId(s.getSection().getId());
			r.setSectionName(s.getSection().getName());
		}
		if(s.getParent() != null){
			r.setParentId(s.getParent().getId());
			r.setParentName(s.getParent().getFirstName()+" "+s.getParent().getLastName());
		}
		if(s.getUser() != null){
			r.setUserId(s.getUser().getId());
			r.setUsername(s.getUser().getUsername());
		}
		return r;
	}
}
