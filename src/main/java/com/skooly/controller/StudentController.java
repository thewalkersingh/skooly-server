package com.skooly.controller;
import com.skooly.dto.common.ApiResponse;
import com.skooly.dto.common.PageResponse;
import com.skooly.dto.request.CreateStudentRequest;
import com.skooly.dto.request.UpdateStudentRequest;
import com.skooly.dto.response.StudentResponse;
import com.skooly.dto.response.StudentSummaryResponse;
import com.skooly.model.Student;
import com.skooly.security.UserPrincipal;
import com.skooly.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {
	private final StudentService studentService;
	
	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
	public ResponseEntity<ApiResponse<PageResponse<StudentSummaryResponse>>> getAllStudents(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) String search,
			@RequestParam(required = false) Long classId,
			@RequestParam(required = false) Long sectionId,
			@RequestParam(required = false) Student.Status status,
			@RequestParam(required = false) Student.Gender gender) {
		
		PageResponse<StudentSummaryResponse> data =
				studentService.getAllStudents(page, size, search, classId, sectionId, status, gender);
		
		return ResponseEntity.ok(new ApiResponse<>(true, "Students fetched successfully", data));
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'PARENT')")
	public ResponseEntity<ApiResponse<StudentResponse>> getStudentById(@PathVariable Long id) {
		return ResponseEntity.ok(
				new ApiResponse<>(true, "Student fetched successfully", studentService.getStudentById(id))
		                        );
	}
	
	@GetMapping("/me")
	@PreAuthorize("hasRole('STUDENT')")
	public ResponseEntity<ApiResponse<StudentResponse>> getMyProfile(
			@AuthenticationPrincipal UserPrincipal userPrincipal) {
		return ResponseEntity.ok(
				new ApiResponse<>(true, "Profile fetched successfully",
				                  studentService.getMyProfile(userPrincipal.getId()))
		                        );
	}
	
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<StudentResponse>> createStudent(
			@Valid @RequestBody CreateStudentRequest request) {
		return ResponseEntity.status(201).body(
				new ApiResponse<>(true, "Student created successfully", studentService.createStudent(request)));
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<StudentResponse>> updateStudent(
			@PathVariable Long id,
			@Valid @RequestBody UpdateStudentRequest request) {
		return ResponseEntity.ok(
				new ApiResponse<>(true, "Student updated successfully", studentService.updateStudent(id, request))
		                        );
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable Long id) {
		studentService.deleteStudent(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Student deleted successfully", null));
	}
	
	@PatchMapping("/{id}/status")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> updateStatus(
			@PathVariable Long id,
			@RequestParam Student.Status status) {
		studentService.updateStatus(id, status);
		return ResponseEntity.ok(new ApiResponse<>(true, "Student status updated", null));
	}
	
	@PostMapping("/{id}/photo")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<StudentResponse>> uploadPhoto(
			@PathVariable Long id,
			@RequestParam("file") MultipartFile file) {
		return ResponseEntity.ok(
				new ApiResponse<>(true, "Photo uploaded successfully", studentService.uploadPhoto(id, file))
		                        );
	}
	
	@DeleteMapping("/{id}/photo")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> deletePhoto(@PathVariable Long id) {
		studentService.deletePhoto(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Photo deleted successfully", null));
	}
}
