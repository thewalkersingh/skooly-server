package com.skooly.controller;

import com.skooly.dto.request.StudentRequest;
import com.skooly.dto.response.SectionResponse;
import com.skooly.dto.response.StudentResponse;
import com.skooly.enums.StudentStatus;
import com.skooly.service.StudentService;
import com.skooly.wrapper.ApiResponse;
import com.skooly.wrapper.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/students")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@Tag(name = "Student Management", description = "Endpoints for managing students")
public class StudentController {
	private final StudentService studentService;
	
	// ── Create / Update / Delete ──────────────────────────────────────────────
	@PostMapping("/{sectionId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ApiResponse<StudentResponse> createStudent(@PathVariable Long sectionId,
		@RequestBody StudentRequest request) {
		log.info("Creating student for section: {}", sectionId);
		StudentResponse response = studentService.createStudent(sectionId, request);
		return ApiResponse.<StudentResponse>builder()
								.success(true)
								.message("Student created successfully")
								.data(response)
								.build();
	}
	
	// PUT /students/{studentId}
	@PutMapping("/{studentId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ApiResponse<StudentResponse> updateStudent(@PathVariable Long studentId,
		@RequestBody StudentRequest request) {
		log.info("Updating student with ID: {}", studentId);
		StudentResponse response = studentService.updateStudent(studentId, request);
		return ApiResponse.<StudentResponse>builder()
								.success(true)
								.message("Student updated successfully")
								.data(response)
								.build();
	}
	
	// DELETE /students/{studentId}
	// Soft delete — sets status to DELETED, data preserved in DB
	@DeleteMapping("/{studentId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ApiResponse<Void> deleteStudent(@PathVariable Long studentId) {
		log.info("Deleting student with ID: {}", studentId);
		studentService.deleteStudent(studentId);
		return ApiResponse.<Void>builder()
								.success(true)
								.message("Student deleted successfully")
								.build();
	}
	
	// ── Single fetch ──────────────────────────────────────────────────────────
	// GET /students/{studentId}
	@GetMapping("/{studentId}")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT', 'STAFF')")
	public ApiResponse<StudentResponse> getStudent(@PathVariable Long studentId) {
		log.info("Fetching student with ID: {}", studentId);
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
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT', 'STAFF')")
	public ApiResponse<StudentResponse> getStudentWithDetails(@PathVariable Long studentId) {
		log.info("Fetching student Details with ID: {}", studentId);
		StudentResponse response = studentService.getStudentWithDetails(studentId);
		return ApiResponse.<StudentResponse>builder()
								.success(true)
								.message("Student Details fetched successfully")
								.data(response)
								.build();
	}
	
	// GET /students/phone/{phone}
	@GetMapping("/phone/{phone}")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT', 'STAFF')")
	public ApiResponse<StudentResponse> getStudentByPhone(@PathVariable String phone) {
		log.info("Fetching student by Phone number: {}", phone);
		StudentResponse response = studentService.getStudentByPhone(phone);
		return ApiResponse.<StudentResponse>builder()
								.success(true)
								.message("Student fetched successfully")
								.data(response)
								.build();
	}
	
	// GET /students/email/{email}
	@GetMapping("/email/{email}")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT', 'STAFF')")
	public ApiResponse<StudentResponse> getStudentByEmail(@PathVariable String email) {
		log.info("Fetching student by Email: {}", email);
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
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT', 'STAFF')")
	public ApiResponse<SectionResponse> getSectionByStudent(@PathVariable Long studentId) {
		log.info("Fetching section for student with ID: {}", studentId);
		SectionResponse response = studentService.getSectionByStudent(studentId);
		return ApiResponse.<SectionResponse>builder()
								.success(true)
								.message("Student section fetched successfully")
								.data(response)
								.build();
	}
	
	// ── Lists ─────────────────────────────────────────────────────────────────
	// GET /students?page=0&size=10
	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT', 'STAFF')")
	public ApiResponse<PageResponse<StudentResponse>> getAllStudents(Pageable pageable) {
		log.info("Fetching all students for page: {}", pageable);
		PageResponse<StudentResponse> response = studentService.getAllStudents(pageable);
		return ApiResponse.<PageResponse<StudentResponse>>builder()
								.data(response)
								.success(true)
								.message("Student fetched successfully")
								.build();
	}
	
	// GET /students/section/{sectionId}?page=0&size=10
	@GetMapping("/section/{sectionId}")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STAFF')")
	public ApiResponse<PageResponse<StudentResponse>> getStudentsBySection(@PathVariable Long sectionId,
		Pageable pageable) {
		log.info("Fetching students for section with ID: {}", sectionId);
		PageResponse<StudentResponse> response = studentService.getStudentsBySection(sectionId, pageable);
		return ApiResponse.<PageResponse<StudentResponse>>builder()
								.data(response)
								.success(true)
								.message("Student fetched successfully")
								.build();
	}
	
	// GET /students/section/{sectionId}/status/{status}?page=0&size=10
	@GetMapping("/section/{sectionId}/status/{status}")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STAFF')")
	public ApiResponse<PageResponse<StudentResponse>> getStudentsBySectionAndStatus(@PathVariable Long sectionId,
		@PathVariable StudentStatus status, Pageable pageable) {
		log.info("Fetching students for section and status with ID: {}", sectionId);
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
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STAFF')")
	public ApiResponse<PageResponse<StudentResponse>> getStudentsByClassroom(@PathVariable Long classroomId,
		Pageable pageable) {
		log.info("Fetching students for classroom with ID: {}", classroomId);
		PageResponse<StudentResponse> response = studentService.getStudentsByClassroom(classroomId, pageable);
		return ApiResponse.<PageResponse<StudentResponse>>builder()
								.data(response)
								.success(true)
								.message("Student fetched successfully")
								.build();
	}
	
	// GET /students/school/{schoolId}?page=0&size=10
	@GetMapping("/school/{schoolId}")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STAFF')")
	public ApiResponse<PageResponse<StudentResponse>> getStudentsBySchool(@PathVariable Long schoolId,
		Pageable pageable) {
		log.info("Fetching students for school with ID: {}", schoolId);
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
	@PreAuthorize("hasAnyRole('ADMIN', 'PARENT', 'STAFF')")
	public ApiResponse<List<StudentResponse>> getStudentsByParent(@PathVariable Long parentId) {
		log.info("Fetching students for parent with ID: {}", parentId);
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
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STAFF')")
	public ApiResponse<PageResponse<StudentResponse>> searchStudentsByName(@PathVariable Long schoolId,
		@RequestParam String name, Pageable pageable) {
		log.info("Searching students by name: {}", name);
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
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	public ApiResponse<StudentResponse> updateStatus(@PathVariable Long studentId, @PathVariable StudentStatus status) {
		log.info("Updating status for student with ID: {}", studentId);
		StudentResponse response = studentService.updateStatus(studentId, status);
		return ApiResponse.<StudentResponse>builder()
								.data(response)
								.success(true)
								.message("Student status updated successfully")
								.build();
	}
	
	// ── Section transfer ──────────────────────────────────────────────────────
	// PATCH /students/{studentId}/transfer/{newSectionId}
	@PatchMapping("/{studentId}/transfer/{newSectionId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ApiResponse<StudentResponse> transferSection(@PathVariable Long studentId, @PathVariable Long newSectionId) {
		log.info("Transferring student with ID: {}", studentId);
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
	@PreAuthorize("hasRole('ADMIN')")
	public ApiResponse<StudentResponse> assignParent(@PathVariable Long studentId, @PathVariable Long parentId) {
		log.info("Assigning parent with ID: {}", parentId);
		StudentResponse response = studentService.assignParent(studentId, parentId);
		return ApiResponse.<StudentResponse>builder()
								.data(response)
								.success(true)
								.message("Student fetched successfully")
								.build();
	}
	
	// DELETE /students/{studentId}/parent
	@DeleteMapping("/{studentId}/parent")
	@PreAuthorize("hasRole('ADMIN')")
	public ApiResponse<StudentResponse> removeParent(@PathVariable Long studentId) {
		log.info("Removing parent for student with ID: {}", studentId);
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
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	public ApiResponse<Long> countStudentsBySection(@PathVariable Long sectionId) {
		log.info("Counting students for section with ID: {}", sectionId);
		return ApiResponse.<Long>builder()
								.data(studentService.countStudentsBySection(sectionId))
								.success(true)
								.message("Student count fetched successfully")
								.build();
	}
	
	// GET /students/school/{schoolId}/count
	@GetMapping("/school/{schoolId}/count")
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	public ApiResponse<Long> countStudentsBySchool(@PathVariable Long schoolId) {
		log.info("Counting students for school with ID: {}", schoolId);
		return ApiResponse.<Long>builder()
								.data(studentService.countStudentsBySchool(schoolId))
								.success(true)
								.message("Student count fetched successfully")
								.build();
	}
	
	// ── Admin utilities ───────────────────────────────────────────────────────
	
	// GET /students/school/{schoolId}/without-parent
	@GetMapping("/school/{schoolId}/without-parent")
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	public ApiResponse<List<StudentResponse>> getStudentsWithoutParent(@PathVariable Long schoolId) {
		log.info("Fetching students without parent for school with ID: {}", schoolId);
		List<StudentResponse> response = studentService.getStudentsWithoutParent(schoolId);
		return ApiResponse.<List<StudentResponse>>builder()
								.data(response)
								.success(true)
								.message("Student fetched successfully")
								.build();
	}
	
}