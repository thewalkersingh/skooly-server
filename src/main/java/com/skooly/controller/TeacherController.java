package com.skooly.controller;
import com.skooly.dto.request.TeacherRequest;
import com.skooly.dto.response.TeacherResponse;
import com.skooly.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/schools/{schoolId}/teachers")
@RequiredArgsConstructor
@Tag(name = "Teachers", description = "Teacher management endpoints")
public class TeacherController {
	private final TeacherService teacherService;
	
	@GetMapping
	@Operation(summary = "Get all teachers")
	public ResponseEntity<List<TeacherResponse>> getAllTeachers(
			@PathVariable Long schoolId,
			@RequestParam(required = false) String search) {
		if(search != null && !search.isBlank()){
			return ResponseEntity.ok(teacherService.searchTeachers(schoolId, search));
		}
		return ResponseEntity.ok(teacherService.getAllTeachers(schoolId));
	}
	
	@GetMapping("/{teacherId}")
	@Operation(summary = "Get teacher by ID")
	public ResponseEntity<TeacherResponse> getTeacherById(
			@PathVariable Long schoolId,
			@PathVariable Long teacherId) {
		return ResponseEntity.ok(teacherService.getTeacherById(schoolId, teacherId));
	}
	
	@GetMapping("/count")
	@Operation(summary = "Get total teacher count")
	public ResponseEntity<Map<String, Long>> getCount(@PathVariable Long schoolId) {
		return ResponseEntity.ok(Map.of("count", teacherService.countTeachers(schoolId)));
	}
	
	@PostMapping
	@Operation(summary = "Create a new teacher")
	public ResponseEntity<TeacherResponse> createTeacher(
			@PathVariable Long schoolId,
			@Valid @RequestBody TeacherRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED)
				       .body(teacherService.createTeacher(schoolId, request));
	}
	
	@PutMapping("/{teacherId}")
	@Operation(summary = "Update a teacher")
	public ResponseEntity<TeacherResponse> updateTeacher(
			@PathVariable Long schoolId,
			@PathVariable Long teacherId,
			@Valid @RequestBody TeacherRequest request) {
		return ResponseEntity.ok(teacherService.updateTeacher(schoolId, teacherId, request));
	}
	
	@DeleteMapping("/{teacherId}")
	@Operation(summary = "Delete a teacher")
	public ResponseEntity<Void> deleteTeacher(
			@PathVariable Long schoolId,
			@PathVariable Long teacherId) {
		teacherService.deleteTeacher(schoolId, teacherId);
		return ResponseEntity.noContent().build();
	}
}