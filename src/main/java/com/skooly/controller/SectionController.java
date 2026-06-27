package com.skooly.controller;

import com.skooly.dto.request.SectionRequest;
import com.skooly.dto.response.SectionResponse;
import com.skooly.mapper.SectionMapper;
import com.skooly.service.SectionService;
import com.skooly.wrapper.ApiResponse;
import com.skooly.wrapper.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sections")
@RequiredArgsConstructor
public class SectionController {
	
	private final SectionService sectionService;
	private final SectionMapper sectionMapper;
	
	@PostMapping("/{classroomId}")
	public ApiResponse<SectionResponse> createSection(@PathVariable Long classroomId,
		 @RequestBody SectionRequest request) {
		SectionResponse response = sectionService.createSection(classroomId, request);
		return ApiResponse.<SectionResponse>builder()
			        .success(true)
			        .message("Section Created Successfully")
			        .data(response)
			        .build();
	}
	
	@PatchMapping("/{sectionId}")
	public ApiResponse<SectionResponse> updateSection(@PathVariable Long sectionId,
		 @RequestBody SectionRequest request) {
		SectionResponse response = sectionService.updateSection(sectionId, request);
		return ApiResponse.<SectionResponse>builder()
			        .success(true)
			        .message("Section Updated Successfully")
			        .data(response)
			        .build();
	}
	
	@DeleteMapping("/{id}")
	public ApiResponse<String> deleteSection(@PathVariable Long id) {
		sectionService.deleteSection(id);
		return ApiResponse.<String>builder()
			        .success(true)
			        .message("Section deleted successfully")
			        .data("Deleted Section with id: " + id)
			        .statusCode(200)
			        .build();
	}
	
	// ── Single fetch ──────────────────────────────────────────────────────────
	@GetMapping("/{id}")
	public ApiResponse<SectionResponse> getSection(@PathVariable Long id) {
		return ApiResponse.<SectionResponse>builder()
			        .success(true)
			        .message("Section fetched successfully")
			        .data(sectionService.getSection(id))
			        .build();
	}
	
	// ── GET /sections/{id}/with-subjects ──────────────────────────────────────
	@GetMapping("/{id}/with-subjects")
	public ApiResponse<SectionResponse> getSectionWithSubjects(@PathVariable Long id) {
		SectionResponse response = sectionService.getSectionWithSubjects(id);
		return ApiResponse.<SectionResponse>builder()
			        .success(true)
			        .message("Section fetched successfully")
			        .data(response)
			        .statusCode(200)
			        .build();
	}
	
	// ── Lists ─────────────────────────────────────────────────────────────────
	// ── GET /sections/classroom/{classroomId} — full list (no pagination) ─────
	@GetMapping("/classroom/{classroomId}")
	public ApiResponse<List<SectionResponse>> getAllSectionByClassroom(@PathVariable Long classroomId) {
		List<SectionResponse> response = sectionService.getSectionsByClassroom(classroomId);
		return ApiResponse.<List<SectionResponse>>builder()
			        .data(response)
			        .success(true)
			        .message("Section with classroom ID" + classroomId + " fetched successfully")
			        .statusCode(200)
			        .build();
	}
	
	// ── GET /sections?page=0&size=10 ──────────────────────────────────────────
	@GetMapping("/classroom/{classroomId}/paged")
	public ApiResponse<PageResponse<SectionResponse>> getSectionsByClassroom(Long classroomId, Pageable pageable) {
		PageResponse<SectionResponse> response = sectionService.getSectionsByClassroom(classroomId, pageable);
		return ApiResponse.<PageResponse<SectionResponse>>builder()
			        .success(true)
			        .message("Sections fetched successfully")
			        .data(response)
			        .statusCode(200)
			        .build();
	}
	
	@GetMapping("/school/{schoolId}")
	public ApiResponse<PageResponse<SectionResponse>> getSectionsBySchool(Long schoolId, Pageable pageable) {
		PageResponse<SectionResponse> response = sectionService.getSectionsBySchool(schoolId, pageable);
		return ApiResponse.<PageResponse<SectionResponse>>builder()
			        .data(response)
			        .success(true)
			        .message("All section fetched successfully for School ID: " + schoolId)
			        .statusCode(200)
			        .build();
	}
	
	@GetMapping("/classroom/{classroomId}/with-subjects")
	public ApiResponse<List<SectionResponse>> getSectionsWithSubjects(@PathVariable Long classroomId) {
		List<SectionResponse> response = sectionService.getSectionsByClassroomWithSubjects(classroomId);
		return ApiResponse.<List<SectionResponse>>builder()
			        .data(response)
			        .message("All Section with Subjects fetched for Classroom ID: " + classroomId)
			        .success(true)
			        .statusCode(200)
			        .build();
	}
	
	// ── Teacher assignment ────────────────────────────────────────────────────
	@PutMapping("/{sectionId}/teacher/{teacherId}")
	public ApiResponse<SectionResponse> assignTeacher(Long sectionId, Long teacherId) {
		SectionResponse response = sectionService.assignTeacher(sectionId, teacherId);
		return ApiResponse.<SectionResponse>builder()
			        .data(response)
			        .message("Teacher with ID " + teacherId + " Assigned to Section with ID " + sectionId)
			        .success(true)
			        .statusCode(200)
			        .build();
	}
	
	@DeleteMapping("/{sectionId}/teacher")
	public ApiResponse<SectionResponse> removeTeacher(Long sectionId) {
		SectionResponse response = sectionService.removeTeacher(sectionId);
		return ApiResponse.<SectionResponse>builder()
			        .data(response)
			        .message("Teacher removed from section with ID: " + sectionId)
			        .success(true)
			        .statusCode(200)
			        .build();
	}
	
	// ── Subject assignment ────────────────────────────────────────────────────
	@PutMapping("/{sectionId}/subjects/{subjectId}")
	public ResponseEntity<ApiResponse<SectionResponse>> addSubject(
		 @PathVariable Long sectionId,
		 @PathVariable Long subjectId) {
		SectionResponse response = sectionService.addSubject(sectionId, subjectId);
		return ResponseEntity.ok(ApiResponse.<SectionResponse>builder()
			                          .success(true)
			                          .message("Subject added successfully")
			                          .data(response)
			                          .statusCode(200)
			                          .build());
	}
	
	@DeleteMapping("/{sectionId}/subjects/{subjectId}")
	public ResponseEntity<ApiResponse<SectionResponse>> removeSubject(
		 @PathVariable Long sectionId,
		 @PathVariable Long subjectId) {
		SectionResponse response = sectionService.removeSubject(sectionId, subjectId);
		return ResponseEntity.ok(ApiResponse.<SectionResponse>builder()
			                          .success(true)
			                          .message("Subject removed successfully")
			                          .data(response)
			                          .statusCode(200)
			                          .build());
	}
	
	// ── Unassigned sections (admin utility) ───────────────────────────────────
	// Returns sections that have no class teacher assigned yet
	@GetMapping("/school/{schoolId}/unassigned")
	public ResponseEntity<ApiResponse<List<SectionResponse>>> getUnassignedSections(
		 @PathVariable Long schoolId) {
		List<SectionResponse> response = sectionService.getUnassignedSections(schoolId);
		return ResponseEntity.ok(ApiResponse.<List<SectionResponse>>builder()
			                          .success(true)
			                          .message("Unassigned sections fetched successfully")
			                          .data(response)
			                          .statusCode(200)
			                          .build());
	}
	
}