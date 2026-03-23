package com.skooly.controller;
import com.skooly.dto.request.SchoolRequest;
import com.skooly.dto.response.SchoolResponse;
import com.skooly.dto.response.SubjectResponse;
import com.skooly.repository.SubjectRepository;
import com.skooly.service.SchoolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/schools")
@RequiredArgsConstructor
@Tag(name = "Schools", description = "School management endpoints")
public class SchoolController {
	private final SchoolService schoolService;
	private final SubjectRepository subjectRepository;
	
	@GetMapping
	@Operation(summary = "Get all schools")
	public ResponseEntity<List<SchoolResponse>> getAllSchools() {
		return ResponseEntity.ok(schoolService.getAllSchools());
	}
	
	@GetMapping("/{id}")
	@Operation(summary = "Get school by ID")
	public ResponseEntity<SchoolResponse> getSchoolById(@PathVariable Long id) {
		return ResponseEntity.ok(schoolService.getSchoolById(id));
	}
	
	@PostMapping
	@Operation(summary = "Create a new school")
	public ResponseEntity<SchoolResponse> createSchool(@Valid @RequestBody SchoolRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(schoolService.createSchool(request));
	}
	
	@PutMapping("/{id}")
	@Operation(summary = "Update a school")
	public ResponseEntity<SchoolResponse> updateSchool(
			@PathVariable Long id,
			@Valid @RequestBody SchoolRequest request) {
		return ResponseEntity.ok(schoolService.updateSchool(id, request));
	}
	
	@DeleteMapping("/{id}")
	@Operation(summary = "Delete a school")
	public ResponseEntity<Void> deleteSchool(@PathVariable Long id) {
		schoolService.deleteSchool(id);
		return ResponseEntity.noContent().build();
	}
	
	// Add endpoint
	@GetMapping("/{schoolId}/subjects")
	public ResponseEntity<List<SubjectResponse>> getAllSubjects(@PathVariable Long schoolId) {
		var subjects = subjectRepository.findBySchoolId(schoolId).stream()
				               .map(s -> {
					               var r = new SubjectResponse();
					               r.setId(s.getId());
					               r.setName(s.getName());
					               r.setCode(s.getCode());
					               return r;
				               }).toList();
		return ResponseEntity.ok(subjects);
	}
}
