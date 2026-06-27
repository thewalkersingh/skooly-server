package com.skooly.service;

import com.skooly.dto.request.SubjectRequest;
import com.skooly.dto.response.SubjectResponse;
import com.skooly.enums.SubjectStatus;
import com.skooly.wrapper.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SubjectService {
	
	// ── Create / Update / Delete ──────────────────────────────────────────────
	SubjectResponse createSubject(SubjectRequest request);
	
	SubjectResponse updateSubject(Long subjectId, SubjectRequest request);
	
	void deleteSubject(Long subjectId);
	
	// ── Single fetch ──────────────────────────────────────────────────────────
	SubjectResponse getSubject(Long subjectId);
	
	SubjectResponse getSubjectByCode(String subjectCode);
	
	// Fetches subject along with its assigned teachers list
	SubjectResponse getSubjectWithTeachers(Long subjectId);
	
	// ── Lists ─────────────────────────────────────────────────────────────────
	PageResponse<SubjectResponse> getAllSubjects(Pageable pageable);
	
	PageResponse<SubjectResponse> getSubjectsByStatus(SubjectStatus status, Pageable pageable);
	
	// Subjects assigned to a section
	List<SubjectResponse> getSubjectsBySection(Long sectionId);
	
	// Subjects taught by a teacher
	List<SubjectResponse> getSubjectsByTeacher(Long teacherId);
	
	// ── Search ────────────────────────────────────────────────────────────────
	PageResponse<SubjectResponse> searchSubjectsByName(String name, Pageable pageable);
	
	// ── Status management ─────────────────────────────────────────────────────
	SubjectResponse updateStatus(Long subjectId, SubjectStatus status);
	
	// ── Teacher assignment ────────────────────────────────────────────────────
	// Adds a teacher to subject_teachers join table
	SubjectResponse assignTeacher(Long subjectId, Long teacherId);
	
	SubjectResponse removeTeacher(Long subjectId, Long teacherId);
	
	// ── Assignment utilities ──────────────────────────────────────────────────
	// Subjects not yet linked to a given section (for assignment dropdown)
	List<SubjectResponse> getSubjectsNotInSection(Long sectionId);
	
	// Subjects not yet assigned to a given teacher
	List<SubjectResponse> getSubjectsNotAssignedToTeacher(Long teacherId);
	
}