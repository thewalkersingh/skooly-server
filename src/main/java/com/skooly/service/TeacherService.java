
package com.skooly.service;
import com.skooly.dto.common.PageResponse;
import com.skooly.dto.request.CreateTeacherRequest;
import com.skooly.dto.request.UpdateTeacherRequest;
import com.skooly.dto.response.TeacherResponse;
import com.skooly.dto.response.TeacherSummaryResponse;
import com.skooly.model.Teacher;
import org.springframework.web.multipart.MultipartFile;

public interface TeacherService {
	PageResponse<TeacherSummaryResponse> getAllTeachers(
			int page, int size, String search,
			Long subjectId, Teacher.Status status, Teacher.Gender gender
	                                                   );
	
	TeacherResponse getTeacherById(Long id);
	
	TeacherResponse getMyProfile(Long userId);
	
	TeacherResponse updateMyProfile(Long userId, UpdateTeacherRequest request);
	
	TeacherResponse createTeacher(CreateTeacherRequest request);
	
	TeacherResponse updateTeacher(Long id, UpdateTeacherRequest request);
	
	void deleteTeacher(Long id);
	
	void updateStatus(Long id, Teacher.Status status);
	
	TeacherResponse uploadPhoto(Long id, MultipartFile file);
	
	void deletePhoto(Long id);
}