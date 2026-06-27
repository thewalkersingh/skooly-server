package com.skooly.controller;

import com.skooly.dto.request.StudentRequest;
import com.skooly.dto.response.SectionResponse;
import com.skooly.dto.response.StudentResponse;
import com.skooly.enums.StudentStatus;
import com.skooly.service.StudentService;
import com.skooly.wrapper.ApiResponse;
import com.skooly.wrapper.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {
	
	private final StudentService studentService;
	
	// ── Create / Update / Delete ──────────────────────────────────────────────
	@PostMapping("/{sectionId}")
	public ApiResponse<StudentResponse> createStudent(@PathVariable Long sectionId,
		@RequestBody StudentRequest request) {
		StudentResponse response = studentService.createStudent(sectionId, request);
		return ApiResponse.<StudentResponse>builder()
		                  .success(true)
		                  .message("Student created successfully")
		                  .data(response)
		                  .build();
	}
	
	// PUT /students/{studentId}
	@PutMapping("/{studentId}")
	public ApiResponse<StudentResponse> updateStudent(@PathVariable Long studentId,
		@RequestBody StudentRequest request) {
		StudentResponse response = studentService.updateStudent(studentId, request);
		return ApiResponse.<StudentResponse>builder()
		                  .success(true)
		                  .message("Student fetched successfully")
		                  .data(response)
		                  .build();
	}
	
	// DELETE /students/{studentId}
	// Soft delete — sets status to DELETED, data preserved in DB
	@DeleteMapping("/{studentId}")
	public ApiResponse<Void> deleteStudent(@PathVariable Long studentId) {
		studentService.deleteStudent(studentId);
		return ApiResponse.<Void>builder().success(true).message("Student fetched successfully").build();
	}
	
	// ── Single fetch ──────────────────────────────────────────────────────────
	// GET /students/{studentId}
	@GetMapping("/{studentId}")
	public ApiResponse<StudentResponse> getStudent(@PathVariable Long studentId) {
		StudentResponse response = studentService.getStudent(studentId);
		return ApiResponse.<StudentResponse>builder()
		                  .success(true)
		                  .message("Student fetched successfully")
		                  .data(response)
		                  .build();
	}
	
	// GET /students/{studentId}/details
	// Fetches student with section + classroom + school in one query
	@GetMapping("/{studentId}/details")
	public ApiResponse<StudentResponse> getStudentWithDetails(@PathVariable Long studentId) {
		StudentResponse response = studentService.getStudentWithDetails(studentId);
		return ApiResponse.<StudentResponse>builder()
		                  .success(true)
		                  .message("Student fetched successfully")
		                  .data(response)
		                  .build();
	}
	
	// GET /students/phone/{phone}
	@GetMapping("/phone/{phone}")
	public ApiResponse<StudentResponse> getStudentByPhone(@PathVariable String phone) {
		StudentResponse response = studentService.getStudentByPhone(phone);
		return ApiResponse.<StudentResponse>builder()
		                  .success(true)
		                  .message("Student fetched successfully")
		                  .data(response)
		                  .build();
	}
	
	// GET /students/email/{email}
	@GetMapping("/email/{email}")
	public ApiResponse<StudentResponse> getStudentByEmail(@PathVariable String email) {
		StudentResponse response = studentService.getStudentByEmail(email);
		return ApiResponse.<StudentResponse>builder()
		                  .success(true)
		                  .message("Student fetched successfully")
		                  .data(response)
		                  .build();
	}
	
	// GET /students/{studentId}/section
	// Returns the section this student belongs to
	@GetMapping("/{studentId}/section")
	public ApiResponse<SectionResponse> getSectionByStudent(@PathVariable Long studentId) {
		SectionResponse response = studentService.getSectionByStudent(studentId);
		return ApiResponse.<SectionResponse>builder()
		                  .success(true)
		                  .message("Student fetched successfully")
		                  .data(response)
		                  .build();
	}
	
	// ── Lists ─────────────────────────────────────────────────────────────────
	// GET /students?page=0&size=10
	@GetMapping
	public ApiResponse<PageResponse<StudentResponse>> getAllStudents(Pageable pageable) {
		PageResponse<StudentResponse> response = studentService.getAllStudents(pageable);
		return ApiResponse.<PageResponse<StudentResponse>>builder()
		                  .data(response)
		                  .success(true)
		                  .message("Student fetched successfully")
		                  .build();
	}
	
	// GET /students/section/{sectionId}?page=0&size=10
	@GetMapping("/section/{sectionId}")
	public ApiResponse<PageResponse<StudentResponse>> getStudentsBySection(@PathVariable Long sectionId,
		Pageable pageable) {
		PageResponse<StudentResponse> response = studentService.getStudentsBySection(sectionId, pageable);
		return ApiResponse.<PageResponse<StudentResponse>>builder()
		                  .data(response)
		                  .success(true)
		                  .message("Student fetched successfully")
		                  .build();
	}
	
	// GET /students/section/{sectionId}/status/{status}?page=0&size=10
	@GetMapping("/section/{sectionId}/status/{status}")
	public ApiResponse<PageResponse<StudentResponse>> getStudentsBySectionAndStatus(@PathVariable Long sectionId,
		@PathVariable StudentStatus status, Pageable pageable) {
		PageResponse<StudentResponse> response =
			studentService.getStudentsBySectionAndStatus(sectionId, status, pageable);
		return ApiResponse.<PageResponse<StudentResponse>>builder()
		                  .data(response)
		                  .success(true)
		                  .message("Student fetched successfully")
		                  .build();
	}
	
	// GET /students/classroom/{classroomId}?page=0&size=10
	@GetMapping("/classroom/{classroomId}")
	public ApiResponse<PageResponse<StudentResponse>> getStudentsByClassroom(@PathVariable Long classroomId,
		Pageable pageable) {
		PageResponse<StudentResponse> response = studentService.getStudentsByClassroom(classroomId, pageable);
		return ApiResponse.<PageResponse<StudentResponse>>builder()
		                  .data(response)
		                  .success(true)
		                  .message("Student fetched successfully")
		                  .build();
	}
	
	// GET /students/school/{schoolId}?page=0&size=10
	@GetMapping("/school/{schoolId}")
	public ApiResponse<PageResponse<StudentResponse>> getStudentsBySchool(@PathVariable Long schoolId,
		Pageable pageable) {
		PageResponse<StudentResponse> response = studentService.getStudentsBySchool(schoolId, pageable);
		return ApiResponse.<PageResponse<StudentResponse>>builder()
		                  .data(response)
		                  .success(true)
		                  .message("Student fetched successfully")
		                  .build();
	}
	
	// Students linked to a parent
	// GET /students/parent/{parentId}
	@GetMapping("/parent/{parentId}")
	public ApiResponse<List<StudentResponse>> getStudentsByParent(@PathVariable Long parentId) {
		List<StudentResponse> response = studentService.getStudentsByParent(parentId);
		return ApiResponse.<List<StudentResponse>>builder()
		                  .data(response)
		                  .success(true)
		                  .message("Student fetched successfully")
		                  .build();
	}
	
	// ── Search ────────────────────────────────────────────────────────────────
	// GET /students/school/{schoolId}/search?name=arjun&page=0&size=10
	@GetMapping("/school/{schoolId}/search")
	public ApiResponse<PageResponse<StudentResponse>> searchStudentsByName(@PathVariable Long schoolId,
		@RequestParam String name, Pageable pageable) {
		PageResponse<StudentResponse> response = studentService.searchStudentsByName(schoolId, name, pageable);
		return ApiResponse.<PageResponse<StudentResponse>>builder()
		                  .data(response)
		                  .success(true)
		                  .message("Student fetched successfully")
		                  .build();
	}
	
	// ── Status management ─────────────────────────────────────────────────────
	// PATCH /students/{studentId}/status/{status}
	@PatchMapping("/{studentId}/status/{status}")
	public ApiResponse<StudentResponse> updateStatus(@PathVariable Long studentId, @PathVariable StudentStatus status) {
		StudentResponse response = studentService.updateStatus(studentId, status);
		return ApiResponse.<StudentResponse>builder()
		                  .data(response)
		                  .success(true)
		                  .message("Student fetched successfully")
		                  .build();
	}
	
	// ── Section transfer ──────────────────────────────────────────────────────
	// PATCH /students/{studentId}/transfer/{newSectionId}
	@PatchMapping("/{studentId}/transfer/{newSectionId}")
	public ApiResponse<StudentResponse> transferSection(@PathVariable Long studentId, @PathVariable Long newSectionId) {
		StudentResponse response = studentService.transferSection(studentId, newSectionId);
		return ApiResponse.<StudentResponse>builder()
		                  .data(response)
		                  .success(true)
		                  .message("Student fetched successfully")
		                  .build();
	}
	
	// ── Parent assignment ─────────────────────────────────────────────────────
	// PUT /students/{studentId}/parent/{parentId}
	@PutMapping("/{studentId}/parent/{parentId}")
	public ApiResponse<StudentResponse> assignParent(@PathVariable Long studentId, @PathVariable Long parentId) {
		StudentResponse response = studentService.assignParent(studentId, parentId);
		return ApiResponse.<StudentResponse>builder()
		                  .data(response)
		                  .success(true)
		                  .message("Student fetched successfully")
		                  .build();
	}
	
	// DELETE /students/{studentId}/parent
	@DeleteMapping("/{studentId}/parent")
	public ApiResponse<StudentResponse> removeParent(@PathVariable Long studentId) {
		StudentResponse response = studentService.removeParent(studentId);
		return ApiResponse.<StudentResponse>builder()
		                  .data(response)
		                  .success(true)
		                  .message("Student fetched successfully")
		                  .build();
	}
	
	// ── Stats ─────────────────────────────────────────────────────────────────
	// GET /students/section/{sectionId}/count
	@GetMapping("/section/{sectionId}/count")
	public ApiResponse<Long> countStudentsBySection(@PathVariable Long sectionId) {
		return ApiResponse.<Long>builder()
		                  .data(studentService.countStudentsBySection(sectionId))
		                  .success(true)
		                  .message("Student fetched successfully")
		                  .build();
	}
	
	// GET /students/school/{schoolId}/count
	@GetMapping("/school/{schoolId}/count")
	public ApiResponse<Long> countStudentsBySchool(@PathVariable Long schoolId) {
		return ApiResponse.<Long>builder()
		                  .data(studentService.countStudentsBySchool(schoolId))
		                  .success(true)
		                  .message("Student fetched successfully")
		                  .build();
	}
	
	// ── Admin utilities ───────────────────────────────────────────────────────
	
	// GET /students/school/{schoolId}/without-parent
	@GetMapping("/school/{schoolId}/without-parent")
	public ApiResponse<List<StudentResponse>> getStudentsWithoutParent(@PathVariable Long schoolId) {
		List<StudentResponse> response = studentService.getStudentsWithoutParent(schoolId);
		return ApiResponse.<List<StudentResponse>>builder()
		                  .data(response)
		                  .success(true)
		                  .message("Student fetched successfully")
		                  .build();
	}
	
}