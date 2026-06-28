package com.skooly.controller;

import com.skooly.dto.request.StaffRequest;
import com.skooly.dto.response.StaffResponse;
import com.skooly.enums.Department;
import com.skooly.enums.StaffRole;
import com.skooly.enums.StaffStatus;
import com.skooly.service.StaffService;
import com.skooly.wrapper.ApiResponse;
import com.skooly.wrapper.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/staff")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class StaffController {
	
	private final StaffService staffService;
	
	// ── Create / Update / Delete ──────────────────────────────────────────────
	// POST /staff/{schoolId}
	@PostMapping("/{schoolId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ApiResponse<StaffResponse> createStaff(@PathVariable Long schoolId, @RequestBody StaffRequest request) {
		
		StaffResponse staffResponse = staffService.createStaff(schoolId, request);
		return ApiResponse.<StaffResponse>builder()
		                  .message("Staff created successfully")
		                  .success(true)
		                  .data(staffResponse)
		                  .build();
	}
	
	// PUT /staff/{staffId}
	@PutMapping("/{staffId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ApiResponse<StaffResponse> updateStaff(@PathVariable Long staffId, @RequestBody StaffRequest request) {
		
		StaffResponse staffResponse = staffService.createStaff(staffId, request);
		return ApiResponse.<StaffResponse>builder()
		                  .message("Staff updated successfully")
		                  .success(true)
		                  .data(staffResponse)
		                  .build();
	}
	
	// DELETE /staff/{staffId}
	// Soft delete — sets status to DELETED, data preserved in DB
	@DeleteMapping("/{staffId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ApiResponse<Void> deleteStaff(@PathVariable Long staffId) {
		
		staffService.deleteStaff(staffId);
		return ApiResponse.<Void>builder()
		                  .message("Staff deleted successfully")
		                  .build();
	}
	
	// ── Single fetch ──────────────────────────────────────────────────────────
	// GET /staff/{staffId}
	@GetMapping("/{staffId}")
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	public ApiResponse<StaffResponse> getStaff(@PathVariable Long staffId) {
		
		StaffResponse response = staffService.getStaff(staffId);
		return ApiResponse.<StaffResponse>builder()
		                  .message("Staff created successfully")
		                  .success(true)
		                  .data(response)
		                  .build();
	}
	
	// GET /staff/phone/{phone}
	@GetMapping("/phone/{phone}")
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	public ApiResponse<StaffResponse> getStaffByPhone(@PathVariable String phone) {
		
		StaffResponse response = staffService.getStaffByPhone(phone);
		return ApiResponse.<StaffResponse>builder()
		                  .message("Staff created successfully")
		                  .success(true)
		                  .data(response)
		                  .build();
	}
	
	// GET /staff/email/{email}
	@GetMapping("/email/{email}")
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	public ApiResponse<StaffResponse> getStaffByEmail(@PathVariable String email) {
		
		StaffResponse response = staffService.getStaffByEmail(email);
		return ApiResponse.<StaffResponse>builder()
		                  .message("Staff created successfully")
		                  .success(true)
		                  .data(response)
		                  .build();
	}
	
	// ── Lists ─────────────────────────────────────────────────────────────────
	// GET /staff?page=0&size=10
	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	public ApiResponse<PageResponse<StaffResponse>> getAllStaff(Pageable pageable) {
		
		PageResponse<StaffResponse> response = staffService.getAllStaff(pageable);
		return ApiResponse.<PageResponse<StaffResponse>>builder()
		                  .message("Staff created successfully")
		                  .success(true)
		                  .data(response)
		                  .build();
	}
	
	// GET /staff/school/{schoolId}?page=0&size=10
	@GetMapping("/school/{schoolId}")
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	public ApiResponse<PageResponse<StaffResponse>> getStaffBySchool(@PathVariable Long schoolId,
		Pageable pageable) {
		
		PageResponse<StaffResponse> response = staffService.getStaffBySchool(schoolId, pageable);
		return ApiResponse.<PageResponse<StaffResponse>>builder()
		                  .message("Staff created successfully")
		                  .success(true)
		                  .data(response)
		                  .build();
	}
	
	// GET /staff/school/{schoolId}/status/{status}?page=0&size=10
	@GetMapping("/school/{schoolId}/status/{status}")
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	public ApiResponse<PageResponse<StaffResponse>> getStaffBySchoolAndStatus(@PathVariable Long schoolId,
		@PathVariable StaffStatus status, Pageable pageable) {
		
		PageResponse<StaffResponse> response = staffService.getStaffBySchoolAndStatus(schoolId, status, pageable);
		return ApiResponse.<PageResponse<StaffResponse>>builder()
		                  .message("Staff created successfully")
		                  .success(true)
		                  .data(response)
		                  .build();
	}
	
	// Filter by role — e.g. all LIBRARIAN staff in a school
	// GET /staff/school/{schoolId}/role/{staffRole}
	@GetMapping("/school/{schoolId}/role/{staffRole}")
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	public ApiResponse<List<StaffResponse>> getStaffByRole(@PathVariable Long schoolId,
		@PathVariable StaffRole staffRole) {
		
		List<StaffResponse> response = staffService.getStaffByRole(schoolId, staffRole);
		return ApiResponse.<List<StaffResponse>>builder()
		                  .message("Staff created successfully")
		                  .success(true)
		                  .data(response)
		                  .build();
	}
	
	// Filter by department — e.g. all FINANCE department staff
	// GET /staff/school/{schoolId}/department/{department}
	@GetMapping("/school/{schoolId}/department/{department}")
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	public ApiResponse<List<StaffResponse>> getStaffByDepartment(@PathVariable Long schoolId,
		@PathVariable Department department) {
		
		List<StaffResponse> response = staffService.getStaffByDepartment(schoolId, department);
		return ApiResponse.<List<StaffResponse>>builder()
		                  .message("Staff created successfully")
		                  .success(true)
		                  .data(response)
		                  .build();
	}
	
	// ── Search ────────────────────────────────────────────────────────────────
	// GET /staff/school/{schoolId}/search?name=john&page=0&size=10
	@GetMapping("/school/{schoolId}/search")
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	public ApiResponse<PageResponse<StaffResponse>> searchStaffByName(@PathVariable Long schoolId,
		@RequestParam String name, Pageable pageable) {
		
		PageResponse<StaffResponse> response = staffService.searchStaffByName(schoolId, name, pageable);
		return ApiResponse.<PageResponse<StaffResponse>>builder()
		                  .message("Staff created successfully")
		                  .success(true)
		                  .data(response)
		                  .build();
	}
	
	// ── Status management ─────────────────────────────────────────────────────
	// PATCH /staff/{staffId}/status/{status}
	@PatchMapping("/{staffId}/status/{status}")
	@PreAuthorize("hasRole('ADMIN')")
	public ApiResponse<StaffResponse> updateStatus(@PathVariable Long staffId, @PathVariable StaffStatus status) {
		
		StaffResponse response = staffService.updateStatus(staffId, status);
		return ApiResponse.<StaffResponse>builder()
		                  .message("Staff created successfully")
		                  .success(true)
		                  .data(response)
		                  .build();
	}
	
	// ── Stats ─────────────────────────────────────────────────────────────────
	// Count of staff per role in a school — useful for admin dashboard
	// GET /staff/school/{schoolId}/role/{staffRole}/count
	@GetMapping("/school/{schoolId}/role/{staffRole}/count")
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	public ApiResponse<Long> countBySchoolAndRole(@PathVariable Long schoolId, @PathVariable StaffRole staffRole) {
		
		Long count = staffService.countBySchoolAndRole(schoolId, staffRole);
		return ApiResponse.<Long>builder()
		                  .message("Staff created successfully")
		                  .success(true)
		                  .data(count)
		                  .build();
	}
	
}