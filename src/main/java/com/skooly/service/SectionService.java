package com.skooly.service;

import com.skooly.dto.request.SectionRequest;
import com.skooly.dto.response.SectionResponse;
import com.skooly.wrapper.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SectionService {
	
	// ── Create / Update / Delete ──────────────────────────────────────────────
	SectionResponse createSection(Long classroomId, SectionRequest request);
	
	SectionResponse updateSection(Long sectionId, SectionRequest request);
	
	void deleteSection(Long sectionId);
	
	// ── Single fetch ──────────────────────────────────────────────────────────
	SectionResponse getSection(Long sectionId);
	
	// Fetches section along with its full subject list (avoids N+1 at detail page)
	SectionResponse getSectionWithSubjects(Long sectionId);
	
	// ── Lists ─────────────────────────────────────────────────────────────────
	List<SectionResponse> getSectionsByClassroom(Long classroomId);
	
	PageResponse<SectionResponse> getSectionsByClassroom(Long classroomId, Pageable pageable);
	
	PageResponse<SectionResponse> getSectionsBySchool(Long schoolId, Pageable pageable);
	
	// Sections where this teacher is class teacher
	List<SectionResponse> getSectionsByTeacher(Long teacherId);
	
	// Sections with their subjects already loaded (for classroom overview screen)
	List<SectionResponse> getSectionsByClassroomWithSubjects(Long classroomId);
	
	// ── Teacher assignment ────────────────────────────────────────────────────
	// Assigns a class teacher to a section (sets section.teacher_id)
	SectionResponse assignTeacher(Long sectionId, Long teacherId);
	
	SectionResponse removeTeacher(Long sectionId);
	
	// ── Subject assignment ────────────────────────────────────────────────────
	SectionResponse addSubject(Long sectionId, Long subjectId);
	
	SectionResponse removeSubject(Long sectionId, Long subjectId);
	
	// ── Unassigned sections (admin utility) ───────────────────────────────────
	// Returns sections that have no class teacher assigned yet
	List<SectionResponse> getUnassignedSections(Long schoolId);
	
}