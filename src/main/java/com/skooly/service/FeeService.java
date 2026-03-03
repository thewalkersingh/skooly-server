package com.skooly.service;
import com.skooly.dto.common.PageResponse;
import com.skooly.dto.request.CreateFeeCategoryRequest;
import com.skooly.dto.request.CreateFeeStructureRequest;
import com.skooly.dto.request.RecordFeePaymentRequest;
import com.skooly.dto.response.*;
import com.skooly.model.FeePayment;

import java.util.List;

public interface FeeService {
	// Fee Categories
	List<FeeCategoryResponse> getAllCategories();
	
	FeeCategoryResponse getCategoryById(Long id);
	
	FeeCategoryResponse createCategory(CreateFeeCategoryRequest request);
	
	FeeCategoryResponse updateCategory(Long id, CreateFeeCategoryRequest request);
	
	void deleteCategory(Long id);
	
	// Fee Structures
	PageResponse<FeeStructureResponse> getAllStructures(int page, int size, Long classId, String academicYear);
	
	FeeStructureResponse getStructureById(Long id);
	
	FeeStructureResponse createStructure(CreateFeeStructureRequest request);
	
	FeeStructureResponse updateStructure(Long id, CreateFeeStructureRequest request);
	
	void deleteStructure(Long id);
	
	// Fee Payments
	PageResponse<FeePaymentResponse> getAllPayments(int page, int size, Long studentId,
			FeePayment.PaymentStatus status,
			Integer month, Integer year);
	
	FeePaymentResponse getPaymentById(Long id);
	
	FeePaymentResponse recordPayment(RecordFeePaymentRequest request);
	
	FeePaymentResponse updatePayment(Long id, RecordFeePaymentRequest request);
	
	void deletePayment(Long id);
	
	// Reports
	FinanceSummaryResponse getFinanceSummary(Integer month, Integer year);
	
	List<FeePaymentResponse> getDefaulters();
}