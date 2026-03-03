package com.skooly.controller;
import com.skooly.dto.common.ApiResponse;
import com.skooly.dto.common.PageResponse;
import com.skooly.dto.request.CreateFeeCategoryRequest;
import com.skooly.dto.request.CreateFeeStructureRequest;
import com.skooly.dto.request.RecordFeePaymentRequest;
import com.skooly.dto.response.*;
import com.skooly.model.FeePayment;
import com.skooly.service.FeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FeeController {
	private final FeeService feeService;
	
	// ── Fee Categories ───────────────────────────────────────────────────────
	
	@GetMapping("/fee-categories")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<List<FeeCategoryResponse>>> getAllCategories() {
		return ResponseEntity.ok(new ApiResponse<>(true, "Fee categories fetched successfully",
		                                           feeService.getAllCategories()));
	}
	
	@GetMapping("/fee-categories/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<FeeCategoryResponse>> getCategoryById(@PathVariable Long id) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Fee category fetched successfully",
		                                           feeService.getCategoryById(id)));
	}
	
	@PostMapping("/fee-categories")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<FeeCategoryResponse>> createCategory(
			@Valid @RequestBody CreateFeeCategoryRequest request) {
		return ResponseEntity.status(201).body(new ApiResponse<>(true, "Fee category created successfully",
		                                                         feeService.createCategory(request)));
	}
	
	@PutMapping("/fee-categories/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<FeeCategoryResponse>> updateCategory(
			@PathVariable Long id, @Valid @RequestBody CreateFeeCategoryRequest request) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Fee category updated successfully",
		                                           feeService.updateCategory(id, request)));
	}
	
	@DeleteMapping("/fee-categories/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
		feeService.deleteCategory(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Fee category deleted successfully", null));
	}
	
	// ── Fee Structures ───────────────────────────────────────────────────────
	
	@GetMapping("/fee-structures")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<PageResponse<FeeStructureResponse>>> getAllStructures(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) Long classId,
			@RequestParam(required = false) String academicYear) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Fee structures fetched successfully",
		                                           feeService.getAllStructures(page, size, classId, academicYear)));
	}
	
	@GetMapping("/fee-structures/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<FeeStructureResponse>> getStructureById(@PathVariable Long id) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Fee structure fetched successfully",
		                                           feeService.getStructureById(id)));
	}
	
	@PostMapping("/fee-structures")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<FeeStructureResponse>> createStructure(
			@Valid @RequestBody CreateFeeStructureRequest request) {
		return ResponseEntity.status(201).body(new ApiResponse<>(true, "Fee structure created successfully",
		                                                         feeService.createStructure(request)));
	}
	
	@PutMapping("/fee-structures/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<FeeStructureResponse>> updateStructure(
			@PathVariable Long id, @Valid @RequestBody CreateFeeStructureRequest request) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Fee structure updated successfully",
		                                           feeService.updateStructure(id, request)));
	}
	
	@DeleteMapping("/fee-structures/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> deleteStructure(@PathVariable Long id) {
		feeService.deleteStructure(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Fee structure deleted successfully", null));
	}
	
	// ── Fee Payments ─────────────────────────────────────────────────────────
	
	@GetMapping("/fee-payments")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<PageResponse<FeePaymentResponse>>> getAllPayments(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) Long studentId,
			@RequestParam(required = false) FeePayment.PaymentStatus status,
			@RequestParam(required = false) Integer month,
			@RequestParam(required = false) Integer year) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Fee payments fetched successfully",
		                                           feeService.getAllPayments(page, size, studentId, status, month,
		                                                                     year)));
	}
	
	@GetMapping("/fee-payments/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<FeePaymentResponse>> getPaymentById(@PathVariable Long id) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Fee payment fetched successfully",
		                                           feeService.getPaymentById(id)));
	}
	
	@PostMapping("/fee-payments")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<FeePaymentResponse>> recordPayment(
			@Valid @RequestBody RecordFeePaymentRequest request) {
		return ResponseEntity.status(201).body(new ApiResponse<>(true, "Payment recorded successfully",
		                                                         feeService.recordPayment(request)));
	}
	
	@PutMapping("/fee-payments/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<FeePaymentResponse>> updatePayment(
			@PathVariable Long id, @Valid @RequestBody RecordFeePaymentRequest request) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Payment updated successfully",
		                                           feeService.updatePayment(id, request)));
	}
	
	@DeleteMapping("/fee-payments/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> deletePayment(@PathVariable Long id) {
		feeService.deletePayment(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Payment deleted successfully", null));
	}
	
	// ── Finance Reports ──────────────────────────────────────────────────────
	
	@GetMapping("/finance/summary")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<FinanceSummaryResponse>> getFinanceSummary(
			@RequestParam(required = false) Integer month,
			@RequestParam(required = false) Integer year) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Finance summary fetched successfully",
		                                           feeService.getFinanceSummary(month, year)));
	}
	
	@GetMapping("/finance/defaulters")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<List<FeePaymentResponse>>> getDefaulters() {
		return ResponseEntity.ok(new ApiResponse<>(true, "Defaulters fetched successfully",
		                                           feeService.getDefaulters()));
	}
}