package com.skooly.controller;
import com.skooly.dto.request.AttendanceRequest;
import com.skooly.dto.response.AttendanceResponse;
import com.skooly.dto.response.AttendanceSummaryResponse;
import com.skooly.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/schools/{schoolId}/attendance")
@RequiredArgsConstructor
@Tag(name = "Attendance", description = "Attendance management endpoints")
public class AttendanceController {
	private final AttendanceService attendanceService;
	
	@PostMapping
	@Operation(summary = "Mark attendance for a class on a date (bulk)")
	public ResponseEntity<List<AttendanceResponse>> markAttendance(
			@PathVariable Long schoolId,
			@Valid @RequestBody AttendanceRequest req) {
		return ResponseEntity.ok(attendanceService.markAttendance(schoolId, req));
	}
	
	@GetMapping
	@Operation(summary = "Get attendance by class and date")
	public ResponseEntity<List<AttendanceResponse>> getByClassAndDate(
			@PathVariable Long schoolId,
			@RequestParam Long classId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		return ResponseEntity.ok(attendanceService.getByClassAndDate(schoolId, classId, date));
	}
	
	@GetMapping("/student/{studentId}")
	@Operation(summary = "Get attendance history for a student")
	public ResponseEntity<List<AttendanceResponse>> getByStudent(
			@PathVariable Long schoolId,
			@PathVariable Long studentId) {
		return ResponseEntity.ok(attendanceService.getByStudent(schoolId, studentId));
	}
	
	@GetMapping("/summary")
	@Operation(summary = "Get today's attendance summary")
	public ResponseEntity<AttendanceSummaryResponse> getTodaySummary(
			@PathVariable Long schoolId) {
		return ResponseEntity.ok(attendanceService.getTodaySummary(schoolId));
	}
	
	@GetMapping("/range")
	@Operation(summary = "Get attendance for a class between two dates")
	public ResponseEntity<List<AttendanceResponse>> getByDateRange(
			@PathVariable Long schoolId,
			@RequestParam Long classId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
		return ResponseEntity.ok(attendanceService.getByClassAndDateRange(schoolId, classId, from, to));
	}
	
}