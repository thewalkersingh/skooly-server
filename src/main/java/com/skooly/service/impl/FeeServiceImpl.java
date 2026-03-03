package com.skooly.service.impl;
import com.skooly.dto.common.PageResponse;
import com.skooly.dto.request.CreateFeeCategoryRequest;
import com.skooly.dto.request.CreateFeeStructureRequest;
import com.skooly.dto.request.RecordFeePaymentRequest;
import com.skooly.dto.response.*;
import com.skooly.exception.BadRequestException;
import com.skooly.exception.ResourceNotFoundException;
import com.skooly.mapper.FeeMapper;
import com.skooly.model.*;
import com.skooly.repository.*;
import com.skooly.service.FeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FeeServiceImpl implements FeeService {
	private final FeeCategoryRepository feeCategoryRepository;
	private final FeeStructureRepository feeStructureRepository;
	private final FeePaymentRepository feePaymentRepository;
	private final StudentRepository studentRepository;
	private final SchoolClassRepository classRepository;
	private final FeeMapper feeMapper;
	
	// ── Fee Categories ───────────────────────────────────────────────────────
	
	@Override
	@Transactional(readOnly = true)
	public List<FeeCategoryResponse> getAllCategories() {
		return feeCategoryRepository.findAll().stream().map(feeMapper::toCategoryResponse).toList();
	}
	
	@Override
	@Transactional(readOnly = true)
	public FeeCategoryResponse getCategoryById(Long id) {
		return feeMapper.toCategoryResponse(findCategoryById(id));
	}
	
	@Override
	public FeeCategoryResponse createCategory(CreateFeeCategoryRequest request) {
		if(feeCategoryRepository.existsByName(request.getName())){
			throw new BadRequestException("Fee category '"+request.getName()+"' already exists");
		}
		return feeMapper.toCategoryResponse(feeCategoryRepository.save(feeMapper.toCategoryEntity(request)));
	}
	
	@Override
	public FeeCategoryResponse updateCategory(Long id, CreateFeeCategoryRequest request) {
		FeeCategory category = findCategoryById(id);
		category.setName(request.getName());
		category.setAmount(request.getAmount());
		if(request.getDescription() != null)
			category.setDescription(request.getDescription());
		return feeMapper.toCategoryResponse(feeCategoryRepository.save(category));
	}
	
	@Override
	public void deleteCategory(Long id) {
		if(!feeCategoryRepository.existsById(id)){
			throw new ResourceNotFoundException("Fee category not found with id: "+id);
		}
		feeCategoryRepository.deleteById(id);
	}
	
	// ── Fee Structures ───────────────────────────────────────────────────────
	
	@Override
	@Transactional(readOnly = true)
	public PageResponse<FeeStructureResponse> getAllStructures(
			int page, int size, Long classId, String academicYear) {
		
		Pageable pageable = PageRequest.of(page-1, size);
		Page<FeeStructure> structures;
		
		if(classId != null){
			structures = feeStructureRepository.findBySchoolClassId(classId, pageable);
		} else if(academicYear != null){
			structures = feeStructureRepository.findByAcademicYear(academicYear, pageable);
		} else{
			structures = feeStructureRepository.findAll(pageable);
		}
		
		List<FeeStructureResponse> data = structures.getContent()
				                                  .stream().map(feeMapper::toStructureResponse).toList();
		return new PageResponse<>(data, page, size, structures.getTotalElements(), structures.getTotalPages());
	}
	
	@Override
	@Transactional(readOnly = true)
	public FeeStructureResponse getStructureById(Long id) {
		return feeMapper.toStructureResponse(findStructureById(id));
	}
	
	@Override
	public FeeStructureResponse createStructure(CreateFeeStructureRequest request) {
		FeeStructure structure = feeMapper.toStructureEntity(request);
		structure.setSchoolClass(findClassById(request.getClassId()));
		structure.setFeeCategory(findCategoryById(request.getFeeCategoryId()));
		return feeMapper.toStructureResponse(feeStructureRepository.save(structure));
	}
	
	@Override
	public FeeStructureResponse updateStructure(Long id, CreateFeeStructureRequest request) {
		FeeStructure structure = findStructureById(id);
		if(request.getClassId() != null)
			structure.setSchoolClass(findClassById(request.getClassId()));
		if(request.getFeeCategoryId() != null)
			structure.setFeeCategory(findCategoryById(request.getFeeCategoryId()));
		if(request.getAcademicYear() != null)
			structure.setAcademicYear(request.getAcademicYear());
		if(request.getDueDate() != null)
			structure.setDueDate(request.getDueDate());
		return feeMapper.toStructureResponse(feeStructureRepository.save(structure));
	}
	
	@Override
	public void deleteStructure(Long id) {
		if(!feeStructureRepository.existsById(id)){
			throw new ResourceNotFoundException("Fee structure not found with id: "+id);
		}
		feeStructureRepository.deleteById(id);
	}
	
	// ── Fee Payments ─────────────────────────────────────────────────────────
	
	@Override
	@Transactional(readOnly = true)
	public PageResponse<FeePaymentResponse> getAllPayments(
			int page, int size, Long studentId,
			FeePayment.PaymentStatus status, Integer month, Integer year) {
		
		Pageable pageable = PageRequest.of(page-1, size, Sort.by("paymentDate").descending());
		Page<FeePayment> payments = feePaymentRepository.findWithFilters(
				studentId, status, month, year, pageable
		                                                                );
		List<FeePaymentResponse> data = payments.getContent()
				                                .stream().map(feeMapper::toPaymentResponse).toList();
		return new PageResponse<>(data, page, size, payments.getTotalElements(), payments.getTotalPages());
	}
	
	@Override
	@Transactional(readOnly = true)
	public FeePaymentResponse getPaymentById(Long id) {
		return feeMapper.toPaymentResponse(findPaymentById(id));
	}
	
	@Override
	public FeePaymentResponse recordPayment(RecordFeePaymentRequest request) {
		FeePayment payment = feeMapper.toPaymentEntity(request);
		payment.setStudent(findStudentById(request.getStudentId()));
		payment.setFeeStructure(findStructureById(request.getFeeStructureId()));
		return feeMapper.toPaymentResponse(feePaymentRepository.save(payment));
	}
	
	@Override
	public FeePaymentResponse updatePayment(Long id, RecordFeePaymentRequest request) {
		FeePayment payment = findPaymentById(id);
		if(request.getAmountPaid() != null)
			payment.setAmountPaid(request.getAmountPaid());
		if(request.getPaymentDate() != null)
			payment.setPaymentDate(request.getPaymentDate());
		if(request.getPaymentMode() != null)
			payment.setPaymentMode(request.getPaymentMode());
		if(request.getTransactionId() != null)
			payment.setTransactionId(request.getTransactionId());
		if(request.getStatus() != null)
			payment.setStatus(request.getStatus());
		return feeMapper.toPaymentResponse(feePaymentRepository.save(payment));
	}
	
	@Override
	public void deletePayment(Long id) {
		if(!feePaymentRepository.existsById(id)){
			throw new ResourceNotFoundException("Fee payment not found with id: "+id);
		}
		feePaymentRepository.deleteById(id);
	}
	
	// ── Finance Reports ──────────────────────────────────────────────────────
	
	@Override
	@Transactional(readOnly = true)
	public FinanceSummaryResponse getFinanceSummary(Integer month, Integer year) {
		BigDecimal collected = month != null && year != null
		                       ? feePaymentRepository.sumCollectedByMonthYear(month, year)
		                       : BigDecimal.ZERO;
		
		if(collected == null)
			collected = BigDecimal.ZERO;
		
		long paid =
				feePaymentRepository.findByStatus(FeePayment.PaymentStatus.PAID, Pageable.unpaged()).getTotalElements();
		long pending =
				feePaymentRepository.findByStatus(FeePayment.PaymentStatus.PENDING, Pageable.unpaged()).getTotalElements();
		long overdue =
				feePaymentRepository.findByStatus(FeePayment.PaymentStatus.OVERDUE, Pageable.unpaged()).getTotalElements();
		
		return FinanceSummaryResponse.builder()
				       .totalCollected(collected)
				       .totalPaid(paid)
				       .totalPending(pending)
				       .totalOverdue(overdue)
				       .build();
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<FeePaymentResponse> getDefaulters() {
		return feePaymentRepository.findAllDefaulters(Pageable.unpaged())
				       .stream().map(feeMapper::toPaymentResponse).toList();
	}
	
	// ── Private helpers ──────────────────────────────────────────────────────
	
	private FeeCategory findCategoryById(Long id) {
		return feeCategoryRepository.findById(id)
				       .orElseThrow(() -> new ResourceNotFoundException("Fee category not found with id: "+id));
	}
	
	private FeeStructure findStructureById(Long id) {
		return feeStructureRepository.findById(id)
				       .orElseThrow(() -> new ResourceNotFoundException("Fee structure not found with id: "+id));
	}
	
	private FeePayment findPaymentById(Long id) {
		return feePaymentRepository.findById(id)
				       .orElseThrow(() -> new ResourceNotFoundException("Fee payment not found with id: "+id));
	}
	
	private Student findStudentById(Long id) {
		return studentRepository.findById(id)
				       .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: "+id));
	}
	
	private SchoolClass findClassById(Long id) {
		return classRepository.findById(id)
				       .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: "+id));
	}
}