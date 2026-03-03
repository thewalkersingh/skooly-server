package com.skooly.controller;
import com.skooly.dto.common.ApiResponse;
import com.skooly.dto.request.BulkTimetableRequest;
import com.skooly.dto.request.CreateTimetableRequest;
import com.skooly.dto.response.TimetableResponse;
import com.skooly.model.Timetable;
import com.skooly.service.TimetableService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/timetable")
@RequiredArgsConstructor
public class TimetableController {
	private final TimetableService timetableService;
	
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<List<TimetableResponse>>> getAllTimetables() {
		return ResponseEntity.ok(new ApiResponse<>(true, "Timetable fetched successfully",
		                                           timetableService.getAllTimetables()));
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<TimetableResponse>> getTimetableById(@PathVariable Long id) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Timetable entry fetched successfully",
		                                           timetableService.getTimetableById(id)));
	}
	
	@GetMapping(params = {"class_id", "section_id"})
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
	public ResponseEntity<ApiResponse<List<TimetableResponse>>> getByClassAndSection(
			@RequestParam("class_id") Long classId,
			@RequestParam("section_id") Long sectionId) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Timetable fetched successfully",
		                                           timetableService.getByClassAndSection(classId, sectionId)));
	}
	
	@GetMapping(params = "teacher_id")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
	public ResponseEntity<ApiResponse<List<TimetableResponse>>> getByTeacher(
			@RequestParam("teacher_id") Long teacherId) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Timetable fetched successfully",
		                                           timetableService.getByTeacher(teacherId)));
	}
	
	@GetMapping(params = "day")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
	public ResponseEntity<ApiResponse<List<TimetableResponse>>> getByDay(
			@RequestParam Timetable.DayOfWeek day) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Timetable fetched successfully",
		                                           timetableService.getByDay(day)));
	}
	
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<TimetableResponse>> createTimetable(
			@Valid @RequestBody CreateTimetableRequest request) {
		return ResponseEntity.status(201).body(new ApiResponse<>(true, "Timetable entry created successfully",
		                                                         timetableService.createTimetable(request)));
	}
	
	@PostMapping("/bulk")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<List<TimetableResponse>>> createBulkTimetable(
			@Valid @RequestBody BulkTimetableRequest request) {
		return ResponseEntity.status(201).body(new ApiResponse<>(true, "Timetable created successfully",
		                                                         timetableService.createBulkTimetable(request)));
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<TimetableResponse>> updateTimetable(
			@PathVariable Long id, @Valid @RequestBody CreateTimetableRequest request) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Timetable entry updated successfully",
		                                           timetableService.updateTimetable(id, request)));
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> deleteTimetable(@PathVariable Long id) {
		timetableService.deleteTimetable(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Timetable entry deleted successfully", null));
	}
}