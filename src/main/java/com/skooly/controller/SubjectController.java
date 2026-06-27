package com.skooly.controller;

import com.skooly.dto.request.SubjectRequest;
import com.skooly.dto.response.SubjectResponse;
import com.skooly.enums.SubjectStatus;
import com.skooly.mapper.SubjectMapper;
import com.skooly.repository.SubjectRepository;
import com.skooly.service.SubjectService;
import com.skooly.wrapper.ApiResponse;
import com.skooly.wrapper.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subjects")
@RequiredArgsConstructor
public class SubjectController {
	
	private final SubjectService subjectService;
	private final SubjectRepository subjectRepository;
	private final SubjectMapper subjectMapper;
	
	// ── Create / Update / Delete ──────────────────────────────────────────────
	// POST /subjects
	@PostMapping
	public ApiResponse<SubjectResponse> createSubject(@RequestBody SubjectRequest request) {
		SubjectResponse response = subjectService.createSubject(request);
		return ApiResponse
			       .<SubjectResponse>builder()
			       .data(response)
			       .message("Subject created")
			       .success(true)
			       .statusCode(201)
			       .build();
	}
	
	// PUT /subjects/{subjectId}
	@PutMapping("/{subjectId}")
	public ApiResponse<SubjectResponse> updateSubject(@PathVariable Long subjectId,
		@RequestBody SubjectRequest request) {
		SubjectResponse response = subjectService.updateSubject(subjectId, request);
		return ApiResponse
			       .<SubjectResponse>builder()
			       .data(response)
			       .message("Subject Updated")
			       .success(true)
			       .statusCode(200)
			       .build();
	}
	
	// DELETE /subjects/{subjectId}
	@DeleteMapping("/{subjectId}")
	public ApiResponse<Void> deleteSubject(@PathVariable Long subjectId) {
		subjectService.deleteSubject(subjectId);
		return ApiResponse.<Void>builder()
			       .message("Subject Deleted")
			       .success(true)
			       .statusCode(200).build();
	}
	
	// ── Single fetch ──────────────────────────────────────────────────────────
	// GET /subjects/{subjectId}
	@GetMapping("/{subjectId}")
	public ApiResponse<SubjectResponse> getSubject(@PathVariable Long subjectId) {
		SubjectResponse response = subjectService.getSubject(subjectId);
		return ApiResponse
			       .<SubjectResponse>builder()
			       .data(response)
			       .message("Subject fetched Successfully")
			       .success(true)
			       .statusCode(200)
			       .build();
	}
	
	// GET /subjects/code/{subjectCode}
	@GetMapping("/code/{subjectCode}")
	public ApiResponse<SubjectResponse> getSubjectByCode(@PathVariable String subjectCode) {
		SubjectResponse response = subjectService.getSubjectByCode(subjectCode);
		return ApiResponse
			       .<SubjectResponse>builder()
			       .data(response)
			       .message("Subject with SubjectCode fetched")
			       .success(true)
			       .statusCode(200)
			       .build();
	}
	
	// GET /subjects/{subjectId}/with-teachers
	@GetMapping("/{subjectId}/with-teachers")
	public ApiResponse<SubjectResponse> getSubjectWithTeachers(@PathVariable Long subjectId) {
		SubjectResponse response = subjectService.getSubjectWithTeachers(subjectId);
		return ApiResponse
			       .<SubjectResponse>builder()
			       .data(response)
			       .message("Subject with Teacher Details fetched")
			       .success(true)
			       .statusCode(200)
			       .build();
	}
	
	// ── Lists ─────────────────────────────────────────────────────────────────
	// GET /subjects?page=0&size=10
	@GetMapping
	public ApiResponse<PageResponse<SubjectResponse>> getAllSubjects(Pageable pageable) {
		PageResponse<SubjectResponse> response = subjectService.getAllSubjects(pageable);
		return ApiResponse
			       .<PageResponse<SubjectResponse>>builder()
			       .data(response)
			       .message("Subject fetched")
			       .success(true)
			       .statusCode(200)
			       .build();
	}
	
	// GET /subjects/status/{status}?page=0&size=10
	@GetMapping("/status/{status}")
	public ApiResponse<PageResponse<SubjectResponse>> getSubjectsByStatus(@PathVariable SubjectStatus status,
		Pageable pageable) {
		PageResponse<SubjectResponse> response = subjectService.getSubjectsByStatus(status, pageable);
		return ApiResponse
			       .<PageResponse<SubjectResponse>>builder()
			       .data(response)
			       .message("Subject fetched")
			       .success(true)
			       .statusCode(200)
			       .build();
	}
	
	// GET /subjects/section/{sectionId}
	@GetMapping("/section/{sectionId}")
	public ApiResponse<List<SubjectResponse>> getSubjectsBySection(@PathVariable Long sectionId) {
		List<SubjectResponse> response = subjectService.getSubjectsBySection(sectionId);
		return ApiResponse
			       .<List<SubjectResponse>>builder()
			       .data(response)
			       .message("Subject fetched")
			       .success(true)
			       .statusCode(200)
			       .build();
	}
	
	// GET /subjects/teacher/{teacherId}
	@GetMapping("/teacher/{teacherId}")
	public ApiResponse<List<SubjectResponse>> getSubjectsByTeacher(@PathVariable Long teacherId) {
		List<SubjectResponse> response = subjectService.getSubjectsByTeacher(teacherId);
		return ApiResponse
			       .<List<SubjectResponse>>builder()
			       .data(response)
			       .message("Subject fetched")
			       .success(true)
			       .statusCode(200)
			       .build();
	}
	
	// ── Search ────────────────────────────────────────────────────────────────
	// GET /subjects/search?name=math&page=0&size=10
	@GetMapping("/search")
	public ApiResponse<PageResponse<SubjectResponse>> searchSubjectsByName(@RequestParam String name,
		Pageable pageable) {
		PageResponse<SubjectResponse> response = subjectService.searchSubjectsByName(name, pageable);
		return ApiResponse
			       .<PageResponse<SubjectResponse>>builder()
			       .data(response)
			       .message("Subject fetched")
			       .success(true)
			       .statusCode(200)
			       .build();
	}
	
	// ── Status management ─────────────────────────────────────────────────────
	// PATCH /subjects/{subjectId}/status/{status}
	@PatchMapping("/{subjectId}/status/{status}")
	public ApiResponse<SubjectResponse> updateStatus(@PathVariable Long subjectId, @PathVariable SubjectStatus status) {
		SubjectResponse response = subjectService.updateStatus(subjectId, status);
		return ApiResponse
			       .<SubjectResponse>builder()
			       .data(response)
			       .message("Subject fetched")
			       .success(true)
			       .statusCode(200)
			       .build();
	}
	
	// ── Teacher assignment ────────────────────────────────────────────────────
	// PUT /subjects/{subjectId}/teachers/{teacherId}
	@PutMapping("/{subjectId}/teachers/{teacherId}")
	public ApiResponse<SubjectResponse> assignTeacher(@PathVariable Long subjectId, @PathVariable Long teacherId) {
		SubjectResponse response = subjectService.assignTeacher(subjectId, teacherId);
		return ApiResponse
			       .<SubjectResponse>builder()
			       .data(response)
			       .message("Subject fetched")
			       .success(true)
			       .statusCode(200)
			       .build();
	}
	
	// DELETE /subjects/{subjectId}/teachers/{teacherId}
	@DeleteMapping("/{subjectId}/teachers/{teacherId}")
	public ApiResponse<SubjectResponse> removeTeacher(@PathVariable Long subjectId, @PathVariable Long teacherId) {
		SubjectResponse response = subjectService.removeTeacher(subjectId, teacherId);
		return ApiResponse
			       .<SubjectResponse>builder()
			       .data(response)
			       .message("Subject fetched")
			       .success(true)
			       .statusCode(200)
			       .build();
	}
	
	// ── Assignment utilities ──────────────────────────────────────────────────
	// GET /subjects/not-in-section/{sectionId}
	@GetMapping("/not-in-section/{sectionId}")
	public ApiResponse<List<SubjectResponse>> getSubjectsNotInSection(@PathVariable Long sectionId) {
		List<SubjectResponse> response = subjectService.getSubjectsNotInSection(sectionId);
		return ApiResponse
			       .<List<SubjectResponse>>builder()
			       .data(response)
			       .message("Subject fetched")
			       .success(true)
			       .statusCode(200)
			       .build();
	}
	
	// GET /subjects/not-assigned-to-teacher/{teacherId}
	@GetMapping("/not-assigned-to-teacher/{teacherId}")
	public ApiResponse<List<SubjectResponse>> getSubjectsNotAssignedToTeacher(@PathVariable Long teacherId) {
		List<SubjectResponse> response = subjectService.getSubjectsNotAssignedToTeacher(teacherId);
		return ApiResponse
			       .<List<SubjectResponse>>builder()
			       .data(response)
			       .message("Subject fetched")
			       .success(true)
			       .statusCode(200)
			       .build();
	}
	
}