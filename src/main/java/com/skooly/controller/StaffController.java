package com.skooly.controller;
import com.skooly.dto.common.ApiResponse;
import com.skooly.dto.common.PageResponse;
import com.skooly.dto.request.*;
import com.skooly.dto.response.*;
import com.skooly.model.LeaveRequest;
import com.skooly.model.Staff;
import com.skooly.security.UserPrincipal;
import com.skooly.service.StaffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class StaffController {
	private final StaffService staffService;
	
	// ── Departments ──────────────────────────────────────────────────────────
	
	@GetMapping("/departments")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<List<DepartmentResponse>>> getAllDepartments() {
		return ResponseEntity.ok(new ApiResponse<>(true, "Departments fetched successfully",
		                                           staffService.getAllDepartments()));
	}
	
	@GetMapping("/departments/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<DepartmentResponse>> getDepartmentById(@PathVariable Long id) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Department fetched successfully",
		                                           staffService.getDepartmentById(id)));
	}
	
	@PostMapping("/departments")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<DepartmentResponse>> createDepartment(
			@Valid @RequestBody CreateDepartmentRequest request) {
		return ResponseEntity.status(201).body(new ApiResponse<>(true, "Department created successfully",
		                                                         staffService.createDepartment(request)));
	}
	
	@PutMapping("/departments/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<DepartmentResponse>> updateDepartment(
			@PathVariable Long id, @Valid @RequestBody CreateDepartmentRequest request) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Department updated successfully",
		                                           staffService.updateDepartment(id, request)));
	}
	
	@DeleteMapping("/departments/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> deleteDepartment(@PathVariable Long id) {
		staffService.deleteDepartment(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Department deleted successfully", null));
	}
	
	// ── Staff ────────────────────────────────────────────────────────────────
	
	@GetMapping("/staff")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<PageResponse<StaffSummaryResponse>>> getAllStaff(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) String search,
			@RequestParam(required = false) Long departmentId,
			@RequestParam(required = false) Staff.StaffStatus status,
			@RequestParam(required = false) Staff.Gender gender) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Staff fetched successfully",
		                                           staffService.getAllStaff(page, size, search, departmentId, status,
		                                                                    gender)));
	}
	
	@GetMapping("/staff/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<StaffResponse>> getStaffById(@PathVariable Long id) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Staff fetched successfully",
		                                           staffService.getStaffById(id)));
	}
	
	@GetMapping("/staff/me")
	@PreAuthorize("hasRole('STAFF')")
	public ResponseEntity<ApiResponse<StaffResponse>> getMyProfile(
			@AuthenticationPrincipal UserPrincipal userPrincipal) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Profile fetched successfully",
		                                           staffService.getMyProfile(userPrincipal.getId())));
	}
	
	@PostMapping("/staff")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<StaffResponse>> createStaff(
			@Valid @RequestBody CreateStaffRequest request) {
		return ResponseEntity.status(201).body(new ApiResponse<>(true, "Staff created successfully",
		                                                         staffService.createStaff(request)));
	}
	
	@PutMapping("/staff/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<StaffResponse>> updateStaff(
			@PathVariable Long id, @Valid @RequestBody UpdateStaffRequest request) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Staff updated successfully",
		                                           staffService.updateStaff(id, request)));
	}
	
	@DeleteMapping("/staff/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> deleteStaff(@PathVariable Long id) {
		staffService.deleteStaff(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Staff deleted successfully", null));
	}
	
	@PatchMapping("/staff/{id}/status")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> updateStatus(
			@PathVariable Long id, @RequestParam Staff.StaffStatus status) {
		staffService.updateStatus(id, status);
		return ResponseEntity.ok(new ApiResponse<>(true, "Staff status updated", null));
	}
	
	@PostMapping("/staff/{id}/photo")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<StaffResponse>> uploadPhoto(
			@PathVariable Long id, @RequestParam("file") MultipartFile file) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Photo uploaded successfully",
		                                           staffService.uploadPhoto(id, file)));
	}
	
	@DeleteMapping("/staff/{id}/photo")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> deletePhoto(@PathVariable Long id) {
		staffService.deletePhoto(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Photo deleted successfully", null));
	}
	
	// ── Leave Requests ───────────────────────────────────────────────────────
	
	@GetMapping("/leave-requests")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<PageResponse<LeaveRequestResponse>>> getAllLeaveRequests(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) Long staffId,
			@RequestParam(required = false) LeaveRequest.LeaveStatus status) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Leave requests fetched successfully",
		                                           staffService.getAllLeaveRequests(page, size, staffId, status)));
	}
	
	@GetMapping("/leave-requests/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	public ResponseEntity<ApiResponse<LeaveRequestResponse>> getLeaveRequestById(@PathVariable Long id) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Leave request fetched successfully",
		                                           staffService.getLeaveRequestById(id)));
	}
	
	@PostMapping("/leave-requests")
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	public ResponseEntity<ApiResponse<LeaveRequestResponse>> createLeaveRequest(
			@Valid @RequestBody CreateLeaveRequest request) {
		return ResponseEntity.status(201).body(new ApiResponse<>(true, "Leave request created successfully",
		                                                         staffService.createLeaveRequest(request)));
	}
	
	@PatchMapping("/leave-requests/{id}/approve")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<LeaveRequestResponse>> approveLeave(
			@PathVariable Long id,
			@AuthenticationPrincipal UserPrincipal userPrincipal) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Leave request approved",
		                                           staffService.approveLeaveRequest(id, userPrincipal.getId())));
	}
	
	@PatchMapping("/leave-requests/{id}/reject")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<LeaveRequestResponse>> rejectLeave(
			@PathVariable Long id,
			@AuthenticationPrincipal UserPrincipal userPrincipal) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Leave request rejected",
		                                           staffService.rejectLeaveRequest(id, userPrincipal.getId())));
	}
	
	@DeleteMapping("/leave-requests/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> deleteLeaveRequest(@PathVariable Long id) {
		staffService.deleteLeaveRequest(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Leave request deleted successfully", null));
	}
	
	// ── Payroll ──────────────────────────────────────────────────────────────
	
	@GetMapping("/payroll")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<PageResponse<PayrollResponse>>> getAllPayrolls(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) Integer month,
			@RequestParam(required = false) Integer year) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Payrolls fetched successfully",
		                                           staffService.getAllPayrolls(page, size, month, year)));
	}
	
	@GetMapping("/payroll/staff/{staffId}")
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	public ResponseEntity<ApiResponse<List<PayrollResponse>>> getPayrollByStaff(@PathVariable Long staffId) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Payroll fetched successfully",
		                                           staffService.getPayrollByStaff(staffId)));
	}
	
	@PostMapping("/payroll")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<PayrollResponse>> createPayroll(
			@Valid @RequestBody CreatePayrollRequest request) {
		return ResponseEntity.status(201).body(new ApiResponse<>(true, "Payroll created successfully",
		                                                         staffService.createPayroll(request)));
	}
	
	@PutMapping("/payroll/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<PayrollResponse>> updatePayroll(
			@PathVariable Long id, @Valid @RequestBody CreatePayrollRequest request) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Payroll updated successfully",
		                                           staffService.updatePayroll(id, request)));
	}
	
	@DeleteMapping("/payroll/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> deletePayroll(@PathVariable Long id) {
		staffService.deletePayroll(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Payroll deleted successfully", null));
	}
}