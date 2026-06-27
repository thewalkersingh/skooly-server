package com.skooly.service;

import com.skooly.dto.request.TeacherRequest;
import com.skooly.dto.response.SectionResponse;
import com.skooly.dto.response.TeacherResponse;
import com.skooly.enums.TeacherStatus;
import com.skooly.wrapper.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TeacherService {
	
	// ── Create / Update / Delete ──────────────────────────────────────────────
	TeacherResponse createTeacher(Long schoolId, TeacherRequest request);
	
	TeacherResponse updateTeacher(Long teacherId, TeacherRequest request);
	
	void deleteTeacher(Long teacherId);
	
	// ── Single fetch ──────────────────────────────────────────────────────────
	TeacherResponse getTeacher(Long teacherId);
	
	TeacherResponse getTeacherByPhone(String phone);
	
	TeacherResponse getTeacherByEmail(String email);
	
	// Class teacher of a specific section
	TeacherResponse getClassTeacherBySection(Long sectionId);
	
	// ── Lists ─────────────────────────────────────────────────────────────────
	PageResponse<TeacherResponse> getAllTeachers(Pageable pageable);
	
	PageResponse<TeacherResponse> getTeachersBySchool(Long schoolId, Pageable pageable);
	
	PageResponse<TeacherResponse> getTeachersBySchoolAndStatus(Long schoolId, TeacherStatus status, Pageable pageable);
	
	// Teachers assigned to a specific subject
	List<TeacherResponse> getTeachersBySubject(Long subjectId);
	
	// ── Search ────────────────────────────────────────────────────────────────
	PageResponse<TeacherResponse> searchTeachersByName(Long schoolId, String name, Pageable pageable);
	
	// ── Status management ─────────────────────────────────────────────────────
	TeacherResponse updateStatus(Long teacherId, TeacherStatus status);
	
	// ── Unassigned teachers (admin utility) ───────────────────────────────────
	// Returns active teachers not yet assigned as class teacher to any section
	List<TeacherResponse> getUnassignedTeachers(Long schoolId);
	
	// Sections where this teacher is class teacher
	List<SectionResponse> getSectionsByTeacher(Long teacherId);
	
}