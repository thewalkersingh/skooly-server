package com.skooly.service;
import com.skooly.dto.request.TeacherRequest;
import com.skooly.dto.response.TeacherResponse;

import java.util.List;

public interface TeacherService {
	List<TeacherResponse> getAllTeachers(Long schoolId);
	TeacherResponse getTeacherById(Long schoolId, Long teacherId);
	List<TeacherResponse> searchTeachers(Long schoolId, String query);
	long countTeachers(Long schoolId);
	TeacherResponse createTeacher(Long schoolId, TeacherRequest request);
	TeacherResponse updateTeacher(Long schoolId, Long teacherId, TeacherRequest request);
	void deleteTeacher(Long schoolId, Long teacherId);
}
