package com.skooly.controller;

import com.skooly.dto.request.TeacherRequest;
import com.skooly.dto.response.SectionResponse;
import com.skooly.dto.response.TeacherResponse;
import com.skooly.enums.TeacherStatus;
import com.skooly.mapper.TeacherMapper;
import com.skooly.service.TeacherService;
import com.skooly.wrapper.ApiResponse;
import com.skooly.wrapper.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/teachers")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class TeacherController {
	
	private final TeacherService teacherService;
	private final TeacherMapper teacherMapper;
	
	// ── Create / Update / Delete ──────────────────────────────────────────────
	@PostMapping("/{schoolId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ApiResponse<TeacherResponse> createTeacher(@PathVariable Long schoolId,
		@RequestBody TeacherRequest request) {
		TeacherResponse response = teacherService.createTeacher(schoolId, request);
		return ApiResponse
			       .<TeacherResponse>builder()
			       .success(true)
			       .message("Teacher created successfully")
			       .data(response)
			       .build();
	}
	
	// PUT /teachers/{teacherId}
	@PutMapping("/{teacherId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ApiResponse<TeacherResponse> updateTeacher(@PathVariable Long teacherId,
		@RequestBody TeacherRequest request) {
		TeacherResponse response = teacherService.updateTeacher(teacherId, request);
		return ApiResponse
			       .<TeacherResponse>builder()
			       .success(true)
			       .message("Teacher Updated successfully")
			       .data(response)
			       .build();
	}
	
	// Soft delete — sets status to DELETED, data preserved in DB
	@DeleteMapping("/{teacherId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ApiResponse<Void> deleteTeacher(@PathVariable Long teacherId) {
		teacherService.deleteTeacher(teacherId);
		return ApiResponse.<Void>builder().success(true).message("Teacher Deleted successfully").build();
	}
	
	// ── Single fetch ──────────────────────────────────────────────────────────
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STAFF')")
	public ApiResponse<TeacherResponse> getTeacherById(@PathVariable Long id) {
		TeacherResponse response = teacherService.getTeacher(id);
		return ApiResponse
			       .<TeacherResponse>builder()
			       .success(true)
			       .message("Teacher fetched successfully")
			       .data(response)
			       .build();
	}
	
	// GET /teachers/phone/{phone}
	@GetMapping("/phone/{phone}")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STAFF')")
	public ApiResponse<TeacherResponse> getTeacherByPhone(@PathVariable String phone) {
		TeacherResponse response = teacherService.getTeacherByPhone(phone);
		return ApiResponse
			       .<TeacherResponse>builder()
			       .data(response)
			       .success(true)
			       .message("Teacher fetched successfully")
			       .data(response)
			       .build();
	}
	
	// GET /teachers/email/{email}
	@GetMapping("/email/{email}")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STAFF')")
	public ApiResponse<TeacherResponse> getTeacherByEmail(@PathVariable String email) {
		TeacherResponse response = teacherService.getTeacherByEmail(email);
		return ApiResponse
			       .<TeacherResponse>builder()
			       .data(response)
			       .success(true)
			       .message("Teacher fetched successfully")
			       .data(response)
			       .build();
	}
	
	// GET /teachers/section/{sectionId}/class-teacher
	@GetMapping("/section/{sectionId}/class-teacher")
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	public ApiResponse<TeacherResponse> getClassTeacherBySection(@PathVariable Long sectionId) {
		TeacherResponse response = teacherService.getClassTeacherBySection(sectionId);
		return ApiResponse
			       .<TeacherResponse>builder()
			       .data(response)
			       .success(true)
			       .message("Teacher fetched successfully")
			       .data(response)
			       .build();
	}
	
	// ── Lists ─────────────────────────────────────────────────────────────────
	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STAFF')")
	public ApiResponse<PageResponse<TeacherResponse>> getAllTeachers(Pageable pageable) {
		PageResponse<TeacherResponse> response = teacherService.getAllTeachers(pageable);
		return ApiResponse
			       .<PageResponse<TeacherResponse>>builder()
			       .data(response)
			       .success(true)
			       .message("Teachers fetched successfully")
			       .build();
	}
	
	// GET /teachers/school/{schoolId}?page=0&size=10
	@GetMapping("/school/{schoolId}")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STAFF')")
	public ApiResponse<PageResponse<TeacherResponse>> getTeachersBySchool(@PathVariable Long schoolId,
		Pageable pageable) {
		PageResponse<TeacherResponse> response = teacherService.getTeachersBySchool(schoolId, pageable);
		return ApiResponse
			       .<PageResponse<TeacherResponse>>builder()
			       .data(response)
			       .success(true)
			       .message("Teachers fetched successfully")
			       .build();
	}
	
	// GET /teachers/school/{schoolId}/status/{status}?page=0&size=10
	@GetMapping("/school/{schoolId}/status/{status}")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STAFF')")
	public ApiResponse<PageResponse<TeacherResponse>> getTeachersBySchoolAndStatus(@PathVariable Long schoolId,
		@PathVariable TeacherStatus status, Pageable pageable) {
		
		PageResponse<TeacherResponse> response = teacherService.getTeachersBySchoolAndStatus(schoolId, status, pageable);
		return ApiResponse
			       .<PageResponse<TeacherResponse>>builder()
			       .data(response)
			       .success(true)
			       .message("Teachers fetched successfully")
			       .build();
	}
	
	// GET /teachers/subject/{subjectId}
	@GetMapping("/subject/{subjectId}")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STAFF')")
	public ApiResponse<List<TeacherResponse>> getTeachersBySubject(@PathVariable Long subjectId) {
		
		List<TeacherResponse> responses = teacherService.getTeachersBySubject(subjectId);
		return ApiResponse
			       .<List<TeacherResponse>>builder()
			       .data(responses)
			       .success(true)
			       .message("Teachers fetched successfully")
			       .build();
	}
	
	// GET /teachers/{teacherId}/sections
	// Sections where this teacher is assigned as class teacher
	@GetMapping("/{teacherId}/sections")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STAFF')")
	public ApiResponse<List<SectionResponse>> getSectionsByTeacher(@PathVariable Long teacherId) {
		
		List<SectionResponse> responses = teacherService.getSectionsByTeacher(teacherId);
		return ApiResponse
			       .<List<SectionResponse>>builder()
			       .data(responses)
			       .success(true)
			       .message("Teachers fetched successfully")
			       .build();
	}
	
	// ── Search ────────────────────────────────────────────────────────────────
	// GET /teachers/school/{schoolId}/search?name=ravi&page=0&size=10
	@GetMapping("/school/{schoolId}/search")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STAFF')")
	public ApiResponse<PageResponse<TeacherResponse>> searchTeachersByName(@PathVariable Long schoolId,
		@RequestParam String name, Pageable pageable) {
		
		PageResponse<TeacherResponse> response = teacherService.searchTeachersByName(schoolId, name, pageable);
		return ApiResponse
			       .<PageResponse<TeacherResponse>>builder()
			       .data(response)
			       .success(true)
			       .message("Teachers fetched successfully")
			       .build();
	}
	
	// ── Status management ─────────────────────────────────────────────────────
	// PATCH /teachers/{teacherId}/status/{status}
	@PatchMapping("/{teacherId}/status/{status}")
	@PreAuthorize("hasRole('ADMIN')")
	public ApiResponse<TeacherResponse> updateStatus(@PathVariable Long teacherId, @PathVariable TeacherStatus status) {
		return ApiResponse.<TeacherResponse>builder()
		                  .data(teacherService.updateStatus(teacherId, status))
		                  .success(true)
		                  .message("Teachers fetched successfully")
		                  .build();
	}
	
	// ── Unassigned teachers ───────────────────────────────────────────────────
	// GET /teachers/school/{schoolId}/unassigned
	// Returns active teachers not yet assigned as class teacher to any section
	@GetMapping("/school/{schoolId}/unassigned")
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	public ApiResponse<List<TeacherResponse>> getUnassignedTeachers(@PathVariable Long schoolId) {
		List<TeacherResponse> responses = teacherService.getUnassignedTeachers(schoolId);
		return ApiResponse.<List<TeacherResponse>>builder()
		                  .data(responses)
		                  .success(true)
		                  .message("Teachers fetched successfully")
		                  .build();
	}
	
}