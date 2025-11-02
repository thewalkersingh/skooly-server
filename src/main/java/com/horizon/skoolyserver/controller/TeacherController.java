package com.horizon.skoolyserver.controller;
import com.horizon.skoolyserver.dto.TeacherDTO;
import com.horizon.skoolyserver.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeacherController {
	private final TeacherService teacherService;
	
	@PostMapping
	public ResponseEntity<TeacherDTO> createTeacher(@Valid @RequestBody TeacherDTO teacherDTO) {
		TeacherDTO created = teacherService.createTeacher(teacherDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}
	
	@GetMapping
	public ResponseEntity<List<TeacherDTO>> getAllTeachers() {
		return ResponseEntity.ok(teacherService.getAllTeachers());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<TeacherDTO> getTeacherById(@PathVariable Long id) {
		return ResponseEntity.ok(teacherService.getTeacherById(id));
	}
	
	@GetMapping("/code/{empCode}")
	public ResponseEntity<TeacherDTO> getTeacherByEmployeeCode(@PathVariable String empCode) {
		return ResponseEntity.ok(teacherService.getTeacherByEmployeeCode(empCode));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<TeacherDTO> updateTeacherById(@PathVariable Long id,
			@Valid @RequestBody TeacherDTO teacherDTO) {
		return ResponseEntity.ok(teacherService.updateTeacher(id, teacherDTO));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTeacherById(@PathVariable Long id) {
		teacherService.deleteTeacher(id);
		return ResponseEntity.noContent().build();
	}
}
