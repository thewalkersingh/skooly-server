package com.horizon.skoolyserver.controller;
import com.horizon.skoolyserver.dto.TeacherDTO;
import com.horizon.skoolyserver.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeacherController {
	private final TeacherService teacherService;
	
	@PostMapping
	public ResponseEntity<TeacherDTO> createTeacher(@RequestBody TeacherDTO teacherDTO) {
		return ResponseEntity.ok(teacherService.createTeacher(teacherDTO));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<TeacherDTO> getTeacherById(@PathVariable Long id) {
		return ResponseEntity.ok(teacherService.getTeacherById(id));
	}
	
	@PutMapping("{id}")
	public ResponseEntity<TeacherDTO> updateTeacherById(@PathVariable Long id, @RequestBody TeacherDTO teacherDTO) {
		return ResponseEntity.ok(teacherService.updateTeacher(id, teacherDTO));
	}
	
	@DeleteMapping("{/id}")
	public ResponseEntity<Void> deleteTeacherById(@PathVariable Long id) {
		teacherService.deleteTeacher(id);
		return ResponseEntity.noContent().build();
	}
}
