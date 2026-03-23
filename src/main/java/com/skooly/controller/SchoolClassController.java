package com.skooly.controller;
import com.skooly.dto.request.SchoolClassRequest;
import com.skooly.dto.request.SectionRequest;
import com.skooly.dto.response.SchoolClassResponse;
import com.skooly.dto.response.SectionResponse;
import com.skooly.service.SchoolClassService;
import com.skooly.service.SectionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/schools/{schoolId}")
@RequiredArgsConstructor
@Tag(name = "Classes & Sections")
public class SchoolClassController {
	private final SchoolClassService classService;
	private final SectionService sectionService;
	
	@GetMapping("/classes")
	public ResponseEntity<List<SchoolClassResponse>> getAllClasses(@PathVariable Long schoolId) {
		return ResponseEntity.ok(classService.getAllClasses(schoolId));
	}
	
	@PostMapping("/classes")
	public ResponseEntity<SchoolClassResponse> createClass(
			@PathVariable Long schoolId,
			@Valid @RequestBody SchoolClassRequest req) {
		return ResponseEntity.status(HttpStatus.CREATED).body(classService.createClass(schoolId, req));
	}
	
	@PutMapping("/classes/{classId}")
	public ResponseEntity<SchoolClassResponse> updateClass(
			@PathVariable Long schoolId,
			@PathVariable Long classId,
			@Valid @RequestBody SchoolClassRequest req) {
		return ResponseEntity.ok(classService.updateClass(schoolId, classId, req));
	}
	
	@DeleteMapping("/classes/{classId}")
	public ResponseEntity<Void> deleteClass(
			@PathVariable Long schoolId,
			@PathVariable Long classId) {
		classService.deleteClass(schoolId, classId);
		return ResponseEntity.noContent().build();
	}
	
	// ── Sections ─────────────────────────────────────────
	
	@GetMapping("/sections")
	public ResponseEntity<List<SectionResponse>> getAllSections(@PathVariable Long schoolId) {
		return ResponseEntity.ok(sectionService.getAllSections(schoolId));
	}
	
	@GetMapping("/classes/{classId}/sections")
	public ResponseEntity<List<SectionResponse>> getSectionsByClass(
			@PathVariable Long schoolId,
			@PathVariable Long classId) {
		return ResponseEntity.ok(sectionService.getSectionsByClass(schoolId, classId));
	}
	
	@PostMapping("/sections")
	public ResponseEntity<SectionResponse> createSection(
			@PathVariable Long schoolId,
			@Valid @RequestBody SectionRequest req) {
		return ResponseEntity.status(HttpStatus.CREATED).body(sectionService.createSection(schoolId, req));
	}
	
	@DeleteMapping("/sections/{sectionId}")
	public ResponseEntity<Void> deleteSection(
			@PathVariable Long schoolId,
			@PathVariable Long sectionId) {
		sectionService.deleteSection(schoolId, sectionId);
		return ResponseEntity.noContent().build();
	}
}