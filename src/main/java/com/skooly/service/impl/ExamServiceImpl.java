package com.skooly.service.impl;
import com.skooly.dto.common.PageResponse;
import com.skooly.dto.request.*;
import com.skooly.dto.response.*;
import com.skooly.exception.BadRequestException;
import com.skooly.exception.ResourceNotFoundException;
import com.skooly.mapper.ExamMapper;
import com.skooly.model.*;
import com.skooly.repository.*;
import com.skooly.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ExamServiceImpl implements ExamService {
	private final ExamRepository examRepository;
	private final ResultRepository resultRepository;
	private final GradeScaleRepository gradeScaleRepository;
	private final StudentRepository studentRepository;
	private final SchoolClassRepository classRepository;
	private final SubjectRepository subjectRepository;
	private final ExamMapper examMapper;
	
	// ── Exams ────────────────────────────────────────────────────────────────
	
	@Override
	@Transactional(readOnly = true)
	public PageResponse<ExamResponse> getAllExams(
			int page, int size, String search,
			Long classId, Long subjectId, String academicYear) {
		
		Pageable pageable = PageRequest.of(page-1, size, Sort.by("examDate").descending());
		Page<Exam> exams = examRepository.findWithFilters(classId, subjectId, academicYear, search, pageable);
		List<ExamResponse> data = exams.getContent().stream().map(examMapper::toExamResponse).toList();
		return new PageResponse<>(data, page, size, exams.getTotalElements(), exams.getTotalPages());
	}
	
	@Override
	@Transactional(readOnly = true)
	public ExamResponse getExamById(Long id) {
		return examMapper.toExamResponse(findExamById(id));
	}
	
	@Override
	public ExamResponse createExam(CreateExamRequest request) {
		Exam exam = examMapper.toExamEntity(request);
		exam.setSchoolClass(findClassById(request.getClassId()));
		exam.setSubject(findSubjectById(request.getSubjectId()));
		return examMapper.toExamResponse(examRepository.save(exam));
	}
	
	@Override
	public ExamResponse updateExam(Long id, CreateExamRequest request) {
		Exam exam = findExamById(id);
		exam.setName(request.getName());
		exam.setExamDate(request.getExamDate());
		exam.setTotalMarks(request.getTotalMarks());
		exam.setPassingMarks(request.getPassingMarks());
		exam.setAcademicYear(request.getAcademicYear());
		if(request.getClassId() != null)
			exam.setSchoolClass(findClassById(request.getClassId()));
		if(request.getSubjectId() != null)
			exam.setSubject(findSubjectById(request.getSubjectId()));
		return examMapper.toExamResponse(examRepository.save(exam));
	}
	
	@Override
	public void deleteExam(Long id) {
		if(!examRepository.existsById(id)){
			throw new ResourceNotFoundException("Exam not found with id: "+id);
		}
		examRepository.deleteById(id);
	}
	
	// ── Results ──────────────────────────────────────────────────────────────
	
	@Override
	@Transactional(readOnly = true)
	public PageResponse<ResultResponse> getResultsByExam(Long examId, int page, int size) {
		Pageable pageable = PageRequest.of(page-1, size, Sort.by("marksObtained").descending());
		Page<Result> results = resultRepository.findByExamId(examId, pageable);
		List<ResultResponse> data = results.getContent().stream().map(examMapper::toResultResponse).toList();
		return new PageResponse<>(data, page, size, results.getTotalElements(), results.getTotalPages());
	}
	
	@Override
	@Transactional(readOnly = true)
	public PageResponse<ResultResponse> getResultsByStudent(Long studentId, int page, int size) {
		Pageable pageable = PageRequest.of(page-1, size, Sort.by("createdAt").descending());
		Page<Result> results = resultRepository.findByStudentId(studentId, pageable);
		List<ResultResponse> data = results.getContent().stream().map(examMapper::toResultResponse).toList();
		return new PageResponse<>(data, page, size, results.getTotalElements(), results.getTotalPages());
	}
	
	@Override
	public ResultResponse createResult(CreateResultRequest request) {
		if(resultRepository.existsByExamIdAndStudentId(request.getExamId(), request.getStudentId())){
			throw new BadRequestException("Result already exists for this student in this exam");
		}
		Result result =
				buildResult(request.getExamId(), request.getStudentId(), request.getMarksObtained(), request.getRemarks());
		return examMapper.toResultResponse(resultRepository.save(result));
	}
	
	@Override
	public List<ResultResponse> createBulkResults(BulkResultRequest request) {
		Exam exam = findExamById(request.getExamId());
		return request.getResults().stream().map(entry -> {
			if(resultRepository.existsByExamIdAndStudentId(exam.getId(), entry.getStudentId())){
				throw new BadRequestException("Result already exists for student ID: "+entry.getStudentId());
			}
			Result result = buildResult(exam.getId(), entry.getStudentId(), entry.getMarksObtained(), entry.getRemarks());
			return examMapper.toResultResponse(resultRepository.save(result));
		}).toList();
	}
	
	@Override
	public ResultResponse updateResult(Long id, CreateResultRequest request) {
		Result result = findResultById(id);
		result.setMarksObtained(request.getMarksObtained());
		if(request.getRemarks() != null)
			result.setRemarks(request.getRemarks());
		applyGradeAndStatus(result, result.getExam());
		return examMapper.toResultResponse(resultRepository.save(result));
	}
	
	@Override
	public void deleteResult(Long id) {
		if(!resultRepository.existsById(id)){
			throw new ResourceNotFoundException("Result not found with id: "+id);
		}
		resultRepository.deleteById(id);
	}
	
	// ── Grade Scale ──────────────────────────────────────────────────────────
	
	@Override
	@Transactional(readOnly = true)
	public List<GradeScaleResponse> getAllGradeScales() {
		return gradeScaleRepository.findAll().stream()
				       .map(g -> GradeScaleResponse.builder()
						                 .id(g.getId()).grade(g.getGrade())
						                 .minMarks(g.getMinMarks()).maxMarks(g.getMaxMarks()).gpa(g.getGpa())
						                 .build())
				       .toList();
	}
	
	@Override
	public GradeScaleResponse createGradeScale(GradeScaleRequest request) {
		GradeScale g = GradeScale.builder()
				               .grade(request.getGrade()).minMarks(request.getMinMarks())
				               .maxMarks(request.getMaxMarks()).gpa(request.getGpa())
				               .build();
		g = gradeScaleRepository.save(g);
		return GradeScaleResponse.builder().id(g.getId()).grade(g.getGrade())
				       .minMarks(g.getMinMarks()).maxMarks(g.getMaxMarks()).gpa(g.getGpa()).build();
	}
	
	@Override
	public GradeScaleResponse updateGradeScale(Long id, GradeScaleRequest request) {
		GradeScale g = gradeScaleRepository.findById(id)
				               .orElseThrow(() -> new ResourceNotFoundException("Grade scale not found with id: "+id));
		g.setGrade(request.getGrade());
		g.setMinMarks(request.getMinMarks());
		g.setMaxMarks(request.getMaxMarks());
		g.setGpa(request.getGpa());
		g = gradeScaleRepository.save(g);
		return GradeScaleResponse.builder().id(g.getId()).grade(g.getGrade())
				       .minMarks(g.getMinMarks()).maxMarks(g.getMaxMarks()).gpa(g.getGpa()).build();
	}
	
	@Override
	public void deleteGradeScale(Long id) {
		if(!gradeScaleRepository.existsById(id)){
			throw new ResourceNotFoundException("Grade scale not found with id: "+id);
		}
		gradeScaleRepository.deleteById(id);
	}
	
	// ── Reports ──────────────────────────────────────────────────────────────
	
	@Override
	@Transactional(readOnly = true)
	public ReportCardResponse getStudentReportCard(Long studentId, String academicYear) {
		Student student = findStudentById(studentId);
		List<Result> results = resultRepository.findByStudentId(studentId);
		
		List<ResultResponse> resultResponses = results.stream()
				                                       .filter(r -> academicYear == null ||
				                                                    r.getExam().getAcademicYear().equals(academicYear))
				                                       .map(examMapper::toResultResponse)
				                                       .toList();
		
		double overallPct = resultResponses.stream()
				                    .mapToDouble(
						                    r -> r.getMarksObtained().doubleValue() / r.getTotalMarks().doubleValue() * 100)
				                    .average().orElse(0.0);
		
		overallPct = BigDecimal.valueOf(overallPct).setScale(2, RoundingMode.HALF_UP).doubleValue();
		
		String overallGrade = gradeScaleRepository
				                      .findByMarks(BigDecimal.valueOf(overallPct))
				                      .map(GradeScale::getGrade).orElse("N/A");
		
		return ReportCardResponse.builder()
				       .studentId(studentId)
				       .studentName(student.getFirstName()+" "+student.getLastName())
				       .className(student.getSchoolClass() != null ? student.getSchoolClass().getName() : null)
				       .sectionName(student.getSection() != null ? student.getSection().getName() : null)
				       .academicYear(academicYear)
				       .results(resultResponses)
				       .overallPercentage(overallPct)
				       .overallGrade(overallGrade)
				       .rank(0) // rank can be computed separately if needed
				       .build();
	}
	
	@Override
	@Transactional(readOnly = true)
	public ExamStatisticsResponse getExamStatistics(Long examId) {
		Exam exam = findExamById(examId);
		long total = resultRepository.countTotalByExam(examId);
		long passed = resultRepository.countPassByExam(examId);
		double avg = resultRepository.getAverageMarksByExam(examId) != null
		             ? resultRepository.getAverageMarksByExam(examId) : 0.0;
		double passPct = total > 0 ? BigDecimal.valueOf(passed * 100.0 / total)
				                             .setScale(2, RoundingMode.HALF_UP).doubleValue() : 0.0;
		
		List<Result> topResults = resultRepository.findByExamIdOrderByMarksDesc(examId);
		String topperName = topResults.isEmpty() ? "N/A" :
		                    topResults.get(0).getStudent().getFirstName()+" "+
		                    topResults.get(0).getStudent().getLastName();
		double topperMarks = topResults.isEmpty() ? 0.0 :
		                     topResults.get(0).getMarksObtained().doubleValue();
		
		return ExamStatisticsResponse.builder()
				       .examId(examId)
				       .examName(exam.getName())
				       .totalStudents(total)
				       .passedStudents(passed)
				       .failedStudents(total-passed)
				       .passPercentage(passPct)
				       .averageMarks(BigDecimal.valueOf(avg).setScale(2, RoundingMode.HALF_UP).doubleValue())
				       .topperName(topperName)
				       .topperMarks(topperMarks)
				       .build();
	}
	
	// ── Private helpers ──────────────────────────────────────────────────────
	
	private Result buildResult(Long examId, Long studentId, BigDecimal marks, String remarks) {
		Exam exam = findExamById(examId);
		Student student = findStudentById(studentId);
		Result result = Result.builder()
				                .exam(exam).student(student)
				                .marksObtained(marks).remarks(remarks)
				                .build();
		applyGradeAndStatus(result, exam);
		return result;
	}
	
	private void applyGradeAndStatus(Result result, Exam exam) {
		String grade = gradeScaleRepository.findByMarks(result.getMarksObtained())
				               .map(GradeScale::getGrade).orElse("N/A");
		result.setGrade(grade);
		result.setStatus(result.getMarksObtained().compareTo(exam.getPassingMarks()) >= 0
		                 ? Result.ResultStatus.PASS : Result.ResultStatus.FAIL);
	}
	
	private Exam findExamById(Long id) {
		return examRepository.findById(id)
				       .orElseThrow(() -> new ResourceNotFoundException("Exam not found with id: "+id));
	}
	
	private Result findResultById(Long id) {
		return resultRepository.findById(id)
				       .orElseThrow(() -> new ResourceNotFoundException("Result not found with id: "+id));
	}
	
	private Student findStudentById(Long id) {
		return studentRepository.findById(id)
				       .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: "+id));
	}
	
	private SchoolClass findClassById(Long id) {
		return classRepository.findById(id)
				       .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: "+id));
	}
	
	private Subject findSubjectById(Long id) {
		return subjectRepository.findById(id)
				       .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: "+id));
	}
}