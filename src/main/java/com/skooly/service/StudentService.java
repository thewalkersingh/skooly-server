package com.skooly.service;

import com.skooly.dto.request.StudentRequest;
import com.skooly.dto.response.SectionResponse;
import com.skooly.dto.response.StudentResponse;
import com.skooly.enums.StudentStatus;
import com.skooly.wrapper.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudentService {
	
	// ── Create / Update / Delete ──────────────────────────────────────────────
	StudentResponse createStudent(Long sectionId, StudentRequest request);
	
	StudentResponse updateStudent(Long studentId, StudentRequest request);
	
	void deleteStudent(Long studentId);
	
	// ── Single fetch ──────────────────────────────────────────────────────────
	StudentResponse getStudent(Long studentId);
	
	// Fetches student with section + classroom + school in one query (detail page)
	StudentResponse getStudentWithDetails(Long studentId);
	
	StudentResponse getStudentByPhone(String phone);
	
	StudentResponse getStudentByEmail(String email);
	
	SectionResponse getSectionByStudent(Long studentId);
	
	// ── Lists ─────────────────────────────────────────────────────────────────
	PageResponse<StudentResponse> getAllStudents(Pageable pageable);
	
	PageResponse<StudentResponse> getStudentsBySection(Long sectionId, Pageable pageable);
	
	PageResponse<StudentResponse> getStudentsBySectionAndStatus(Long sectionId, StudentStatus studentStatus,
		Pageable pageable);
	
	PageResponse<StudentResponse> getStudentsByClassroom(Long classroomId, Pageable pageable);
	
	PageResponse<StudentResponse> getStudentsBySchool(Long schoolId, Pageable pageable);
	
	// Students linked to a parent
	List<StudentResponse> getStudentsByParent(Long parentId);
	
	// ── Search ────────────────────────────────────────────────────────────────
	PageResponse<StudentResponse> searchStudentsByName(Long schoolId, String name, Pageable pageable);
	
	// ── Status management ─────────────────────────────────────────────────────
	StudentResponse updateStatus(Long studentId, StudentStatus studentStatus);
	
	// ── Section transfer ──────────────────────────────────────────────────────
	// Moves a student from their current section to a new one
	StudentResponse transferSection(Long studentId, Long newSectionId);
	
	// ── Parent assignment ─────────────────────────────────────────────────────
	StudentResponse assignParent(Long studentId, Long parentId);
	
	StudentResponse removeParent(Long studentId);
	
	// ── Stats ─────────────────────────────────────────────────────────────────
	long countStudentsBySection(Long sectionId);
	
	long countStudentsBySchool(Long schoolId);
	
	// ── Admin utilities ───────────────────────────────────────────────────────
	// Students with no parent linked yet
	List<StudentResponse> getStudentsWithoutParent(Long schoolId);
	
}