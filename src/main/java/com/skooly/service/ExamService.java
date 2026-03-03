package com.skooly.service;
import com.skooly.dto.common.PageResponse;
import com.skooly.dto.request.*;
import com.skooly.dto.response.*;

import java.util.List;

public interface ExamService {
	// Exams
	PageResponse<ExamResponse> getAllExams(int page, int size, String search,
			Long classId, Long subjectId, String academicYear);
	
	ExamResponse getExamById(Long id);
	
	ExamResponse createExam(CreateExamRequest request);
	
	ExamResponse updateExam(Long id, CreateExamRequest request);
	
	void deleteExam(Long id);
	
	// Results
	PageResponse<ResultResponse> getResultsByExam(Long examId, int page, int size);
	
	PageResponse<ResultResponse> getResultsByStudent(Long studentId, int page, int size);
	
	ResultResponse createResult(CreateResultRequest request);
	
	List<ResultResponse> createBulkResults(BulkResultRequest request);
	
	ResultResponse updateResult(Long id, CreateResultRequest request);
	
	void deleteResult(Long id);
	
	// Grade Scale
	List<GradeScaleResponse> getAllGradeScales();
	
	GradeScaleResponse createGradeScale(GradeScaleRequest request);
	
	GradeScaleResponse updateGradeScale(Long id, GradeScaleRequest request);
	
	void deleteGradeScale(Long id);
	
	// Reports
	ReportCardResponse getStudentReportCard(Long studentId, String academicYear);
	
	ExamStatisticsResponse getExamStatistics(Long examId);
}