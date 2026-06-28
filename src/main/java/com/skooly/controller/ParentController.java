package com.skooly.controller;

import com.skooly.dto.common.StudentSummary;
import com.skooly.dto.request.ParentRequest;
import com.skooly.dto.response.ParentResponse;
import com.skooly.enums.ParentStatus;
import com.skooly.service.ParentService;
import com.skooly.wrapper.ApiResponse;
import com.skooly.wrapper.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parents")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class ParentController {
	
	private final ParentService parentService;
	
	// ── Create / Update / Delete ──────────────────────────────────────────────
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ApiResponse<ParentResponse> createParent(@RequestBody ParentRequest request) {
		
		ParentResponse response = parentService.createParent(request);
		return ApiResponse.<ParentResponse>builder()
		                  .success(true)
		                  .message("Parent created successfully")
		                  .data(response)
		                  .build();
	}
	
	// PUT /parents/{parentId}
	@PutMapping("/{parentId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ApiResponse<ParentResponse> updateParent(@PathVariable Long parentId, @RequestBody ParentRequest request) {
		
		ParentResponse response = parentService.updateParent(parentId, request);
		return ApiResponse.<ParentResponse>builder()
		                  .success(true)
		                  .message("Parent updated successfully")
		                  .data(response)
		                  .build();
	}
	
	// DELETE /parents/{parentId}
	// Soft delete — sets status to DELETED, data preserved in DB
	@DeleteMapping("/{parentId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ApiResponse<Void> deleteParent(@PathVariable Long parentId) {
		
		parentService.deleteParent(parentId);
		return ApiResponse.<Void>builder()
		                  .success(true)
		                  .message("Parent deleted successfully")
		                  .build();
	}
	
	// PATCH /parents/{parentId}/status/{status}
	@PatchMapping("/{parentId}/status/{status}")
	@PreAuthorize("hasRole('ADMIN')")
	public ApiResponse<ParentResponse> updateStatus(@PathVariable Long parentId, @PathVariable ParentStatus status) {
		
		ParentResponse response = parentService.updateStatus(parentId, status);
		return ApiResponse.<ParentResponse>builder()
		                  .success(true)
		                  .message("Parent Status Updated successfully")
		                  .data(response)
		                  .build();
	}
	
	// ── Single fetch ──────────────────────────────────────────────────────────
	// GET /parents/{parentId}
	@GetMapping("/{parentId}")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STAFF', 'PARENT')")
	public ApiResponse<ParentResponse> getParent(@PathVariable Long parentId) {
		
		ParentResponse response = parentService.getParent(parentId);
		return ApiResponse.<ParentResponse>builder()
		                  .success(true)
		                  .message("Parent fetched successfully")
		                  .data(response)
		                  .build();
	}
	
	// GET /parents/phone/{phone}
	@GetMapping("/phone/{phone}")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STAFF', 'PARENT')")
	public ApiResponse<ParentResponse> getParentByPhone(@PathVariable String phone) {
		
		ParentResponse response = parentService.getParentByPhone(phone);
		return ApiResponse.<ParentResponse>builder()
		                  .success(true)
		                  .message("Parent fetched successfully")
		                  .data(response)
		                  .build();
	}
	
	// GET /parents/email/{email}
	@GetMapping("/email/{email}")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STAFF', 'PARENT')")
	public ApiResponse<ParentResponse> getParentByEmail(@PathVariable String email) {
		
		ParentResponse response = parentService.getParentByEmail(email);
		return ApiResponse.<ParentResponse>builder()
		                  .success(true)
		                  .message("Parent fetched successfully")
		                  .data(response)
		                  .build();
	}
	
	// GET /parents/{parentId}/with-identity
	@GetMapping("/{parentId}/with-identity")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STAFF', 'PARENT')")
	public ApiResponse<ParentResponse> getParentWithIdentity(@PathVariable Long parentId) {
		
		ParentResponse response = parentService.getParentWithIdentity(parentId);
		return ApiResponse.<ParentResponse>builder()
		                  .success(true)
		                  .message("Parent fetched successfully")
		                  .data(response)
		                  .build();
	}
	
	// ── Lists ─────────────────────────────────────────────────────────────────
	// GET /parents?page=0&size=10
	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	public ApiResponse<PageResponse<ParentResponse>> getAllParents(Pageable pageable) {
		
		PageResponse<ParentResponse> response = parentService.getAllParents(pageable);
		return ApiResponse.<PageResponse<ParentResponse>>builder()
		                  .success(true)
		                  .message("Parent fetched successfully")
		                  .data(response)
		                  .build();
	}
	
	// GET /parents/{parentId}/students
	// All children linked to this parent
	@GetMapping("/{parentId}/students")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STAFF', 'PARENT')")
	public ApiResponse<List<StudentSummary>> getStudentsByParent(@PathVariable Long parentId) {
		
		List<StudentSummary> response = parentService.getStudentsByParent(parentId);
		
		return ApiResponse.<List<StudentSummary>>builder()
		                  .success(true)
		                  .message("Parent fetched successfully")
		                  .data(response)
		                  .build();
	}
	
	// GET /parents/school/{schoolId}?page=0&size=10
	// All parents with at least one child in this school
	@GetMapping("/school/{schoolId}")
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	public ApiResponse<PageResponse<ParentResponse>> getParentsBySchool(@PathVariable Long schoolId,
		Pageable pageable) {
		
		PageResponse<ParentResponse> response = parentService.getParentsBySchool(schoolId, pageable);
		return ApiResponse.<PageResponse<ParentResponse>>builder()
		                  .success(true)
		                  .message("Parent fetched successfully")
		                  .data(response)
		                  .build();
	}
	
	// GET /parents/school/{schoolId}/multiple-children
	// Parents with more than one child enrolled in the school
	@GetMapping("/school/{schoolId}/multiple-children")
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	public ApiResponse<List<ParentResponse>> getParentsWithMultipleChildren(@PathVariable Long schoolId) {
		
		List<ParentResponse> response = parentService.getParentsWithMultipleChildren(schoolId);
		return ApiResponse.<List<ParentResponse>>builder()
		                  .success(true)
		                  .message("Parent fetched successfully")
		                  .data(response)
		                  .build();
	}
	
	// ── Search ────────────────────────────────────────────────────────────────
	
	// GET /parents/search?name=suresh&page=0&size=10
	@GetMapping("/search")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STAFF', 'PARENT')")
	public ApiResponse<PageResponse<ParentResponse>> searchParentsByName(@RequestParam String name, Pageable pageable) {
		
		PageResponse<ParentResponse> response = parentService.searchParentsByName(name, pageable);
		return ApiResponse.<PageResponse<ParentResponse>>builder()
		                  .success(true)
		                  .message("Parent fetched successfully")
		                  .data(response)
		                  .build();
	}
	
}