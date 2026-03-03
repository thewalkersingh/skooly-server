package com.skooly.controller;
import com.skooly.dto.common.ApiResponse;
import com.skooly.dto.common.PageResponse;
import com.skooly.dto.request.CreateSubjectRequest;
import com.skooly.dto.response.SubjectResponse;
import com.skooly.service.SubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subjects")
@RequiredArgsConstructor
public class SubjectController {
	private final SubjectService subjectService;
	
	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
	public ResponseEntity<ApiResponse<PageResponse<SubjectResponse>>> getAllSubjects(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) String search) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Subjects fetched successfully",
		                                           subjectService.getAllSubjects(page, size, search)));
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
	public ResponseEntity<ApiResponse<SubjectResponse>> getSubjectById(@PathVariable Long id) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Subject fetched successfully",
		                                           subjectService.getSubjectById(id)));
	}
	
	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
	public ResponseEntity<ApiResponse<List<SubjectResponse>>> getSubjectsByClass(
			@RequestParam Long classId) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Subjects fetched successfully",
		                                           subjectService.getSubjectsByClass(classId)));
	}
	
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<SubjectResponse>> createSubject(
			@Valid @RequestBody CreateSubjectRequest request) {
		return ResponseEntity.status(201).body(new ApiResponse<>(true, "Subject created successfully",
		                                                         subjectService.createSubject(request)));
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<SubjectResponse>> updateSubject(
			@PathVariable Long id, @Valid @RequestBody CreateSubjectRequest request) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Subject updated successfully",
		                                           subjectService.updateSubject(id, request)));
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> deleteSubject(@PathVariable Long id) {
		subjectService.deleteSubject(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Subject deleted successfully", null));
	}
}