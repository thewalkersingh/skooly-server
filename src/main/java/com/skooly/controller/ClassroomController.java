package com.skooly.controller;

import com.skooly.dto.request.ClassroomRequest;
import com.skooly.dto.response.ClassroomResponse;
import com.skooly.enums.ClassroomStatus;
import com.skooly.service.ClassroomService;
import com.skooly.wrapper.ApiResponse;
import com.skooly.wrapper.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/classrooms")
@RequiredArgsConstructor
public class ClassroomController {
	
	private final ClassroomService classroomService;
	
	// ── Create / Update / Delete ──────────────────────────────────────────────
	@PostMapping("/{schoolId}")
	public ApiResponse<ClassroomResponse> createClassroom(@PathVariable Long schoolId,
		@RequestBody ClassroomRequest request) {
		ClassroomResponse response = classroomService.createClassroom(schoolId, request);
		return ApiResponse
			       .<ClassroomResponse>builder()
			       .success(true)
			       .message("Classroom created successfully")
			       .data(response)
			       .statusCode(201)
			       .build();
	}
	
	@PutMapping("/{id}/request")
	public ApiResponse<ClassroomResponse> updateClassroom(@PathVariable Long classroomId,
		@RequestBody ClassroomRequest request) {
		ClassroomResponse response = classroomService.updateClassroom(classroomId, request);
		return ApiResponse
			       .<ClassroomResponse>builder()
			       .data(response)
			       .message("Classroom updated for ID: " + classroomId)
			       .success(true)
			       .statusCode(200)
			       .build();
	}
	
	@DeleteMapping("/delete/{id}")
	public ApiResponse<String> deleteClassroom(@PathVariable Long id) {
		classroomService.deleteClassroom(id);
		return ApiResponse
			       .<String>builder()
			       .data("Classroom deleted for ID: " + id)
			       .message("Classroom deleted successfully")
			       .statusCode(200)
			       .success(true)
			       .build();
	}
	
	// ── Single fetch ──────────────────────────────────────────────────────────
	@GetMapping("/id/{classroomId}")
	public ApiResponse<ClassroomResponse> getClassroom(@PathVariable Long classroomId) {
		ClassroomResponse response = classroomService.getClassroom(classroomId);
		return ApiResponse
			       .<ClassroomResponse>builder()
			       .success(true)
			       .message("Classroom fetched successfully")
			       .data(response)
			       .build();
	}
	
	// or we can use this mapping -> GET /classrooms/code/{classroomCode}
	@GetMapping("/code/{classroomCode}")
	public ApiResponse<ClassroomResponse> getClassroomByCode(String classroomCode) {
		ClassroomResponse response = classroomService.getClassroomByCode(classroomCode);
		return ApiResponse.<ClassroomResponse>builder()
			       .data(response)
			       .message("Classroom fetched for ClassCode: " + classroomCode)
			       .success(true)
			       .statusCode(200)
			       .build();
	}
	
	// ── Lists ─────────────────────────────────────────────────────────────────
	@GetMapping("/all")
	public ApiResponse<PageResponse<ClassroomResponse>> getAllClassrooms(Pageable pageable) {
		PageResponse<ClassroomResponse> response = classroomService.getAllClassrooms(pageable);
		return ApiResponse
			       .<PageResponse<ClassroomResponse>>builder()
			       .success(true)
			       .message("Classrooms fetched successfully")
			       .data(response)
			       .build();
	}
	
	@GetMapping("/school/{schoolId}")
	public ApiResponse<PageResponse<ClassroomResponse>> getClassroomsBySchool(@PathVariable Long schoolId,
		Pageable pageable) {
		PageResponse<ClassroomResponse> response = classroomService.getClassroomsBySchool(schoolId, pageable);
		return ApiResponse.<PageResponse<ClassroomResponse>>builder()
			       .data(response)
			       .message("Classroom fetched successfully for School ID: " + schoolId)
			       .success(true)
			       .statusCode(200)
			       .build();
	}
	
	@GetMapping("/school/{schoolId}/status/{status}")
	public ApiResponse<PageResponse<ClassroomResponse>> getClassroomsBySchoolAndStatus(@PathVariable Long schoolId,
		@PathVariable ClassroomStatus status, Pageable pageable) {
		PageResponse<ClassroomResponse> response = classroomService
			                                           .getClassroomsBySchoolAndStatus(schoolId, status, pageable);
		return ApiResponse.<PageResponse<ClassroomResponse>>builder()
			       .data(response)
			       .message("Classroom Fetched successfully for school ID: " + schoolId + " and Status: " + status)
			       .success(true)
			       .statusCode(200)
			       .build();
	}
	
	// ── Status management ─────────────────────────────────────────────────────
	@PatchMapping("/{classroomId}/status/{status}")
	public ApiResponse<ClassroomResponse> updateStatus(@PathVariable Long classroomId,
		@PathVariable ClassroomStatus status) {
		ClassroomResponse response = classroomService.updateStatus(classroomId, status);
		return ApiResponse.<ClassroomResponse>builder()
			       .data(response)
			       .message("Classroom Status Updated for ID: " + classroomId)
			       .success(true)
			       .statusCode(200)
			       .build();
	}
	
}