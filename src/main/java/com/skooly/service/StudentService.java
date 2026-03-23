package com.skooly.service;
import com.skooly.dto.request.StudentRequest;
import com.skooly.dto.response.StudentResponse;

import java.util.List;

public interface StudentService {
	List<StudentResponse> getAllStudents(Long schoolId);
	
	StudentResponse getStudentById(Long schoolId, Long studentId);
	
	List<StudentResponse> searchStudents(Long schoolId, String query);
	
	long countStudents(Long schoolId);
	
	StudentResponse createStudent(Long schoolId, StudentRequest request);
	
	StudentResponse updateStudent(Long schoolId, Long studentId, StudentRequest request);
	
	void deleteStudent(Long schoolId, Long studentId);
}
