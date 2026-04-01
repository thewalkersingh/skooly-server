package com.skooly.controller;
import com.skooly.dto.request.StudentRequest;
import com.skooly.dto.response.StudentResponse;
import com.skooly.service.StudentService;
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
@RequestMapping("/api/v1/schools/{schoolId}/students")
@RequiredArgsConstructor
@Tag(name = "Students", description = "Student management endpoints")
public class StudentController {
	private final StudentService studentService;
	
	@GetMapping
	@Operation(summary = "Get all students for a school")
	public ResponseEntity<List<StudentResponse>> getAllStudents(@PathVariable Long schoolId,
			@RequestParam(required = false) String search) {
		
		if(search != null && !search.isBlank()){
			return ResponseEntity.ok(studentService.searchStudents(schoolId, search));
		}
		return ResponseEntity.ok(studentService.getAllStudents(schoolId));
	}
	
	@GetMapping("/{studentId}")
	@Operation(summary = "Get student by ID")
	public ResponseEntity<StudentResponse> getStudentById(@PathVariable Long schoolId, @PathVariable Long studentId) {
		return ResponseEntity.ok(studentService.getStudentById(schoolId, studentId));
	}
	
	@GetMapping("/count")
	@Operation(summary = "Get total student count")
	public ResponseEntity<Map<String, Long>> getCount(@PathVariable Long schoolId) {
		return ResponseEntity.ok(Map.of("count", studentService.countStudents(schoolId)));
	}
	
	@PostMapping
	@Operation(summary = "Create a new student")
	public ResponseEntity<StudentResponse> createStudent(
			@PathVariable Long schoolId,
			@Valid @RequestBody StudentRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED)
				       .body(studentService.createStudent(schoolId, request));
	}
	
	@PutMapping("/{studentId}")
	@Operation(summary = "Update a student")
	public ResponseEntity<StudentResponse> updateStudent(
			@PathVariable Long schoolId,
			@PathVariable Long studentId,
			@Valid @RequestBody StudentRequest request) {
		return ResponseEntity.ok(studentService.updateStudent(schoolId, studentId, request));
	}
	
	@DeleteMapping("/{studentId}")
	@Operation(summary = "Delete a student")
	public ResponseEntity<Void> deleteStudent(
			@PathVariable Long schoolId,
			@PathVariable Long studentId) {
		studentService.deleteStudent(schoolId, studentId);
		return ResponseEntity.noContent().build();
	}
	
}
