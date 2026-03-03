package com.skooly.controller;
import com.skooly.dto.common.ApiResponse;
import com.skooly.dto.common.PageResponse;
import com.skooly.dto.request.CreateClassRequest;
import com.skooly.dto.response.ClassResponse;
import com.skooly.dto.response.SectionResponse;
import com.skooly.service.ClassService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/classes")
@RequiredArgsConstructor
public class ClassController {
	private final ClassService classService;
	
	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
	public ResponseEntity<ApiResponse<PageResponse<ClassResponse>>> getAllClasses(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) String search) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Classes fetched successfully",
		                                           classService.getAllClasses(page, size, search)));
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
	public ResponseEntity<ApiResponse<ClassResponse>> getClassById(@PathVariable Long id) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Class fetched successfully",
		                                           classService.getClassById(id)));
	}
	
	@GetMapping("/{id}/sections")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
	public ResponseEntity<ApiResponse<PageResponse<SectionResponse>>> getSectionsByClass(
			@PathVariable Long id,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Sections fetched successfully",
		                                           classService.getSectionsByClass(id, page, size)));
	}
	
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<ClassResponse>> createClass(
			@Valid @RequestBody CreateClassRequest request) {
		return ResponseEntity.status(201).body(new ApiResponse<>(true, "Class created successfully",
		                                                         classService.createClass(request)));
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<ClassResponse>> updateClass(
			@PathVariable Long id, @Valid @RequestBody CreateClassRequest request) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Class updated successfully",
		                                           classService.updateClass(id, request)));
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> deleteClass(@PathVariable Long id) {
		classService.deleteClass(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Class deleted successfully", null));
	}
}