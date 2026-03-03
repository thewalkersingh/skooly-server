package com.skooly.controller;
import com.skooly.dto.common.ApiResponse;
import com.skooly.dto.request.BulkAttendanceRequest;
import com.skooly.dto.request.MarkAttendanceRequest;
import com.skooly.dto.request.MarkTeacherAttendanceRequest;
import com.skooly.dto.response.AttendanceResponse;
import com.skooly.dto.response.AttendanceSummaryResponse;
import com.skooly.dto.response.TeacherAttendanceResponse;
import com.skooly.security.UserPrincipal;
import com.skooly.service.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/attendance")
@RequiredArgsConstructor
public class AttendanceController {
	private final AttendanceService attendanceService;
	
	// ── Student Attendance ───────────────────────────────────────────────────
	
	@GetMapping("/students/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
	public ResponseEntity<ApiResponse<AttendanceResponse>> getAttendanceById(@PathVariable Long id) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Attendance fetched successfully",
		                                           attendanceService.getAttendanceById(id)));
	}
	
	@GetMapping("/students")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
	public ResponseEntity<ApiResponse<List<AttendanceResponse>>> getClassAttendanceByDate(
			@RequestParam Long classId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Class attendance fetched successfully",
		                                           attendanceService.getClassAttendanceByDate(classId, date)));
	}
	
	@GetMapping("/students/monthly")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'PARENT', 'STUDENT')")
	public ResponseEntity<ApiResponse<List<AttendanceResponse>>> getStudentMonthlyAttendance(
			@RequestParam Long studentId,
			@RequestParam int month,
			@RequestParam int year) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Monthly attendance fetched successfully",
		                                           attendanceService.getStudentMonthlyAttendance(studentId, month,
		                                                                                         year)));
	}
	
	@GetMapping("/students/summary")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'PARENT', 'STUDENT')")
	public ResponseEntity<ApiResponse<AttendanceSummaryResponse>> getStudentAttendanceSummary(
			@RequestParam Long studentId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Attendance summary fetched successfully",
		                                           attendanceService.getStudentAttendanceSummary(studentId, from, to)));
	}
	
	@GetMapping("/low")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
	public ResponseEntity<ApiResponse<List<AttendanceSummaryResponse>>> getLowAttendanceStudents(
			@RequestParam Long classId,
			@RequestParam int month,
			@RequestParam int year,
			@RequestParam(defaultValue = "75.0") double threshold) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Low attendance students fetched successfully",
		                                           attendanceService.getLowAttendanceStudents(classId, month, year,
		                                                                                      threshold)));
	}
	
	@PostMapping("/students")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
	public ResponseEntity<ApiResponse<AttendanceResponse>> markAttendance(
			@Valid @RequestBody MarkAttendanceRequest request,
			@AuthenticationPrincipal UserPrincipal userPrincipal) {
		return ResponseEntity.status(201).body(new ApiResponse<>(true, "Attendance marked successfully",
		                                                         attendanceService.markAttendance(request,
		                                                                                          userPrincipal.getId())));
	}
	
	@PostMapping("/students/bulk")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
	public ResponseEntity<ApiResponse<List<AttendanceResponse>>> markBulkAttendance(
			@Valid @RequestBody BulkAttendanceRequest request,
			@AuthenticationPrincipal UserPrincipal userPrincipal) {
		return ResponseEntity.status(201).body(new ApiResponse<>(true, "Bulk attendance marked successfully",
		                                                         attendanceService.markBulkAttendance(request,
		                                                                                              userPrincipal.getId())));
	}
	
	@PutMapping("/students/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
	public ResponseEntity<ApiResponse<AttendanceResponse>> updateAttendance(
			@PathVariable Long id,
			@Valid @RequestBody MarkAttendanceRequest request) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Attendance updated successfully",
		                                           attendanceService.updateAttendance(id, request)));
	}
	
	@DeleteMapping("/students/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> deleteAttendance(@PathVariable Long id) {
		attendanceService.deleteAttendance(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Attendance deleted successfully", null));
	}
	
	// ── Teacher Attendance ───────────────────────────────────────────────────
	
	@GetMapping("/teachers/monthly")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
	public ResponseEntity<ApiResponse<List<TeacherAttendanceResponse>>> getTeacherMonthlyAttendance(
			@RequestParam Long teacherId,
			@RequestParam int month,
			@RequestParam int year) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Teacher monthly attendance fetched successfully",
		                                           attendanceService.getTeacherMonthlyAttendance(teacherId, month,
		                                                                                         year)));
	}
	
	@PostMapping("/teachers")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<TeacherAttendanceResponse>> markTeacherAttendance(
			@Valid @RequestBody MarkTeacherAttendanceRequest request) {
		return ResponseEntity.status(201).body(new ApiResponse<>(true, "Teacher attendance marked successfully",
		                                                         attendanceService.markTeacherAttendance(request)));
	}
	
	@PostMapping("/teachers/bulk")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<List<TeacherAttendanceResponse>>> markBulkTeacherAttendance(
			@Valid @RequestBody List<MarkTeacherAttendanceRequest> requests) {
		return ResponseEntity.status(201).body(new ApiResponse<>(true, "Bulk teacher attendance marked successfully",
		                                                         attendanceService.markBulkTeacherAttendance(requests)));
	}
	
	@PutMapping("/teachers/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<TeacherAttendanceResponse>> updateTeacherAttendance(
			@PathVariable Long id,
			@Valid @RequestBody MarkTeacherAttendanceRequest request) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Teacher attendance updated successfully",
		                                           attendanceService.updateTeacherAttendance(id, request)));
	}
	
	@DeleteMapping("/teachers/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> deleteTeacherAttendance(@PathVariable Long id) {
		attendanceService.deleteTeacherAttendance(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Teacher attendance deleted successfully", null));
	}
}