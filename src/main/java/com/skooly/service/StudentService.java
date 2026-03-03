
package com.skooly.service;
import com.skooly.dto.common.PageResponse;
import com.skooly.dto.request.CreateStudentRequest;
import com.skooly.dto.request.UpdateStudentRequest;
import com.skooly.dto.response.StudentResponse;
import com.skooly.dto.response.StudentSummaryResponse;
import com.skooly.model.Student;
import org.springframework.web.multipart.MultipartFile;

public interface StudentService {
	PageResponse<StudentSummaryResponse> getAllStudents(
			int page, int size, String search,
			Long classId, Long sectionId,
			Student.Status status, Student.Gender gender
	                                                   );
	
	StudentResponse getStudentById(Long id);
	
	StudentResponse getMyProfile(Long userId);
	
	StudentResponse createStudent(CreateStudentRequest request);
	
	StudentResponse updateStudent(Long id, UpdateStudentRequest request);
	
	void deleteStudent(Long id);
	
	void updateStatus(Long id, Student.Status status);
	
	StudentResponse uploadPhoto(Long id, MultipartFile file);
	
	void deletePhoto(Long id);
}