package com.horizon.skoolyserver.controller;
import com.horizon.skoolyserver.dto.SchoolClassDTO;
import com.horizon.skoolyserver.service.SchoolClassService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/classes")
@AllArgsConstructor
public class SchoolClassController {
	private final SchoolClassService classService;
	
	@GetMapping
	public ResponseEntity<List<SchoolClassDTO>> getAll() {
		return ResponseEntity.ok(classService.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<SchoolClassDTO> getById(@PathVariable Long id) {
		return ResponseEntity.ok(classService.findById(id));
	}
	
	@PostMapping
	public ResponseEntity<SchoolClassDTO> create(@RequestBody SchoolClassDTO schoolClass) {
		return ResponseEntity.ok(classService.create(schoolClass));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable Long id) {
		classService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{grade}")
	public ResponseEntity<List<SchoolClassDTO>> getByGrade(@PathVariable String grade) {
		return ResponseEntity.ok(classService.findByGrade(grade));
	}
	
	@GetMapping("/{section}")
	public ResponseEntity<List<SchoolClassDTO>> getBySection(@PathVariable String section) {
		return ResponseEntity.ok(classService.findBySection(section));
	}
}
