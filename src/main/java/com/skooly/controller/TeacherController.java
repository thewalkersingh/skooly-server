// TeacherController.java
package com.skooly.controller;
import com.skooly.dto.common.ApiResponse;
import com.skooly.dto.common.PageResponse;
import com.skooly.dto.request.CreateTeacherRequest;
import com.skooly.dto.request.UpdateTeacherRequest;
import com.skooly.dto.response.TeacherResponse;
import com.skooly.dto.response.TeacherSummaryResponse;
import com.skooly.model.Teacher;
import com.skooly.security.UserPrincipal;
import com.skooly.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/teachers")
@RequiredArgsConstructor
public class TeacherController {
	private final TeacherService teacherService;
	
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<PageResponse<TeacherSummaryResponse>>> getAllTeachers(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) String search,
			@RequestParam(required = false) Long subjectId,
			@RequestParam(required = false) Teacher.Status status,
			@RequestParam(required = false) Teacher.Gender gender) {
		
		PageResponse<TeacherSummaryResponse> data =
				teacherService.getAllTeachers(page, size, search, subjectId, status, gender);
		
		return ResponseEntity.ok(new ApiResponse<>(true, "Teachers fetched successfully", data));
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
	public ResponseEntity<ApiResponse<TeacherResponse>> getTeacherById(@PathVariable Long id) {
		return ResponseEntity.ok(
				new ApiResponse<>(true, "Teacher fetched successfully", teacherService.getTeacherById(id))
		                        );
	}
	
	@GetMapping("/me")
	@PreAuthorize("hasRole('TEACHER')")
	public ResponseEntity<ApiResponse<TeacherResponse>> getMyProfile(
			@AuthenticationPrincipal UserPrincipal userPrincipal) {
		return ResponseEntity.ok(
				new ApiResponse<>(true, "Profile fetched successfully",
				                  teacherService.getMyProfile(userPrincipal.getId()))
		                        );
	}
	
	@PutMapping("/me")
	@PreAuthorize("hasRole('TEACHER')")
	public ResponseEntity<ApiResponse<TeacherResponse>> updateMyProfile(
			@AuthenticationPrincipal UserPrincipal userPrincipal,
			@Valid @RequestBody UpdateTeacherRequest request) {
		return ResponseEntity.ok(
				new ApiResponse<>(true, "Profile updated successfully",
				                  teacherService.updateMyProfile(userPrincipal.getId(), request))
		                        );
	}
	
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<TeacherResponse>> createTeacher(
			@Valid @RequestBody CreateTeacherRequest request) {
		return ResponseEntity.status(201).body(
				new ApiResponse<>(true, "Teacher created successfully", teacherService.createTeacher(request))
		                                      );
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<TeacherResponse>> updateTeacher(
			@PathVariable Long id,
			@Valid @RequestBody UpdateTeacherRequest request) {
		return ResponseEntity.ok(
				new ApiResponse<>(true, "Teacher updated successfully", teacherService.updateTeacher(id, request))
		                        );
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> deleteTeacher(@PathVariable Long id) {
		teacherService.deleteTeacher(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Teacher deleted successfully", null));
	}
	
	@PatchMapping("/{id}/status")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> updateStatus(
			@PathVariable Long id,
			@RequestParam Teacher.Status status) {
		teacherService.updateStatus(id, status);
		return ResponseEntity.ok(new ApiResponse<>(true, "Teacher status updated", null));
	}
	
	@PostMapping("/{id}/photo")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<TeacherResponse>> uploadPhoto(
			@PathVariable Long id,
			@RequestParam("file") MultipartFile file) {
		return ResponseEntity.ok(
				new ApiResponse<>(true, "Photo uploaded successfully", teacherService.uploadPhoto(id, file))
		                        );
	}
	
	@DeleteMapping("/{id}/photo")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> deletePhoto(@PathVariable Long id) {
		teacherService.deletePhoto(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Photo deleted successfully", null));
	}
}