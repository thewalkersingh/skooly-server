package com.skooly.controller;
import com.skooly.dto.common.ApiResponse;
import com.skooly.dto.common.PageResponse;
import com.skooly.dto.request.CreateSectionRequest;
import com.skooly.dto.response.SectionResponse;
import com.skooly.service.SectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sections")
@RequiredArgsConstructor
public class SectionController {
	private final SectionService sectionService;
	
	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
	public ResponseEntity<ApiResponse<PageResponse<SectionResponse>>> getAllSections(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) Long classId) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Sections fetched successfully",
		                                           sectionService.getAllSections(page, size, classId)));
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
	public ResponseEntity<ApiResponse<SectionResponse>> getSectionById(@PathVariable Long id) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Section fetched successfully",
		                                           sectionService.getSectionById(id)));
	}
	
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<SectionResponse>> createSection(
			@Valid @RequestBody CreateSectionRequest request) {
		return ResponseEntity.status(201).body(new ApiResponse<>(true, "Section created successfully",
		                                                         sectionService.createSection(request)));
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<SectionResponse>> updateSection(
			@PathVariable Long id, @Valid @RequestBody CreateSectionRequest request) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Section updated successfully",
		                                           sectionService.updateSection(id, request)));
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> deleteSection(@PathVariable Long id) {
		sectionService.deleteSection(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Section deleted successfully", null));
	}
	
	@PatchMapping("/{id}/teacher")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<SectionResponse>> assignTeacher(
			@PathVariable Long id, @RequestParam Long teacherId) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Teacher assigned successfully",
		                                           sectionService.assignTeacher(id, teacherId)));
	}
}