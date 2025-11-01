package com.horizon.skoolyserver.controller;
import com.horizon.skoolyserver.dto.StudentDTO;
import com.horizon.skoolyserver.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {
	private final StudentService studentService;
	
	@PostMapping
	public ResponseEntity<StudentDTO> createStudent(@RequestBody StudentDTO dto) {
		return ResponseEntity.ok(studentService.createStudent(dto));
	}
	
	@GetMapping
	public ResponseEntity<List<StudentDTO>> getAllStudents() {
		return ResponseEntity.ok(studentService.getAllStudents());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
		return ResponseEntity.ok(studentService.getStudentById(id));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<StudentDTO> updateStudent(@PathVariable Long id, @RequestBody StudentDTO dto) {
		return ResponseEntity.ok(studentService.updateStudent(id, dto));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
		studentService.deleteStudent(id);
		return ResponseEntity.noContent().build();
	}
}