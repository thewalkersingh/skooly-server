package com.skooly.controller;
import com.skooly.dto.common.ApiResponse;
import com.skooly.dto.common.PageResponse;
import com.skooly.dto.request.*;
import com.skooly.dto.response.*;
import com.skooly.service.ExamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ExamController {
	private final ExamService examService;
	
	// ── Exams ────────────────────────────────────────────────────────────────
	
	@GetMapping("/exams")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
	public ResponseEntity<ApiResponse<PageResponse<ExamResponse>>> getAllExams(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) String search,
			@RequestParam(required = false) Long classId,
			@RequestParam(required = false) Long subjectId,
			@RequestParam(required = false) String academicYear) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Exams fetched successfully",
		                                           examService.getAllExams(page, size, search, classId, subjectId,
		                                                                   academicYear)));
	}
	
	@GetMapping("/exams/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
	public ResponseEntity<ApiResponse<ExamResponse>> getExamById(@PathVariable Long id) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Exam fetched successfully",
		                                           examService.getExamById(id)));
	}
	
	@PostMapping("/exams")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<ExamResponse>> createExam(
			@Valid @RequestBody CreateExamRequest request) {
		return ResponseEntity.status(201).body(new ApiResponse<>(true, "Exam created successfully",
		                                                         examService.createExam(request)));
	}
	
	@PutMapping("/exams/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<ExamResponse>> updateExam(
			@PathVariable Long id, @Valid @RequestBody CreateExamRequest request) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Exam updated successfully",
		                                           examService.updateExam(id, request)));
	}
	
	@DeleteMapping("/exams/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> deleteExam(@PathVariable Long id) {
		examService.deleteExam(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Exam deleted successfully", null));
	}
	
	@GetMapping("/exams/{id}/statistics")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
	public ResponseEntity<ApiResponse<ExamStatisticsResponse>> getExamStatistics(@PathVariable Long id) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Exam statistics fetched successfully",
		                                           examService.getExamStatistics(id)));
	}
	
	// ── Results ──────────────────────────────────────────────────────────────
	
	@GetMapping("/results")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
	public ResponseEntity<ApiResponse<PageResponse<ResultResponse>>> getResultsByExam(
			@RequestParam Long examId,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Results fetched successfully",
		                                           examService.getResultsByExam(examId, page, size)));
	}
	
	@GetMapping("/results/student")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'PARENT', 'STUDENT')")
	public ResponseEntity<ApiResponse<PageResponse<ResultResponse>>> getResultsByStudent(
			@RequestParam Long studentId,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Results fetched successfully",
		                                           examService.getResultsByStudent(studentId, page, size)));
	}
	
	@PostMapping("/results")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
	public ResponseEntity<ApiResponse<ResultResponse>> createResult(
			@Valid @RequestBody CreateResultRequest request) {
		return ResponseEntity.status(201).body(new ApiResponse<>(true, "Result added successfully",
		                                                         examService.createResult(request)));
	}
	
	@PostMapping("/results/bulk")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
	public ResponseEntity<ApiResponse<List<ResultResponse>>> createBulkResults(
			@Valid @RequestBody BulkResultRequest request) {
		return ResponseEntity.status(201).body(new ApiResponse<>(true, "Bulk results added successfully",
		                                                         examService.createBulkResults(request)));
	}
	
	@PutMapping("/results/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
	public ResponseEntity<ApiResponse<ResultResponse>> updateResult(
			@PathVariable Long id, @Valid @RequestBody CreateResultRequest request) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Result updated successfully",
		                                           examService.updateResult(id, request)));
	}
	
	@DeleteMapping("/results/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> deleteResult(@PathVariable Long id) {
		examService.deleteResult(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Result deleted successfully", null));
	}
	
	// ── Grade Scale ──────────────────────────────────────────────────────────
	
	@GetMapping("/grade-scale")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
	public ResponseEntity<ApiResponse<List<GradeScaleResponse>>> getAllGradeScales() {
		return ResponseEntity.ok(new ApiResponse<>(true, "Grade scale fetched successfully",
		                                           examService.getAllGradeScales()));
	}
	
	@PostMapping("/grade-scale")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<GradeScaleResponse>> createGradeScale(
			@Valid @RequestBody GradeScaleRequest request) {
		return ResponseEntity.status(201).body(new ApiResponse<>(true, "Grade scale created successfully",
		                                                         examService.createGradeScale(request)));
	}
	
	@PutMapping("/grade-scale/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<GradeScaleResponse>> updateGradeScale(
			@PathVariable Long id, @Valid @RequestBody GradeScaleRequest request) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Grade scale updated successfully",
		                                           examService.updateGradeScale(id, request)));
	}
	
	@DeleteMapping("/grade-scale/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> deleteGradeScale(@PathVariable Long id) {
		examService.deleteGradeScale(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Grade scale deleted successfully", null));
	}
	
	// ── Report Card ──────────────────────────────────────────────────────────
	
	@GetMapping("/students/{studentId}/report-card")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'PARENT', 'STUDENT')")
	public ResponseEntity<ApiResponse<ReportCardResponse>> getStudentReportCard(
			@PathVariable Long studentId,
			@RequestParam(required = false) String academicYear) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Report card fetched successfully",
		                                           examService.getStudentReportCard(studentId, academicYear)));
	}
}