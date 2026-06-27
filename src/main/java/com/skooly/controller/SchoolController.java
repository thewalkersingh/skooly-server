package com.skooly.controller;

import com.skooly.dto.request.SchoolRequest;
import com.skooly.dto.response.SchoolResponse;
import com.skooly.enums.SchoolStatus;
import com.skooly.exception.ResourceNotFoundException;
import com.skooly.service.SchoolService;
import com.skooly.wrapper.ApiResponse;
import com.skooly.wrapper.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schools")
@RequiredArgsConstructor
public class SchoolController {
	
	private final SchoolService schoolService;
	
	@PostMapping
	public ApiResponse<SchoolResponse> createSchool(@RequestBody SchoolRequest request) {
		SchoolResponse response = schoolService.createSchool(request);
		return ApiResponse.<SchoolResponse>builder()
			       .success(true)
			       .message("School created successfully")
			       .data(response)
			       .build();
	}
	
	@PatchMapping("/{id}/request")
	public ApiResponse<SchoolResponse> updateSchool(@PathVariable Long id, @RequestBody SchoolRequest request) {
		SchoolResponse response = schoolService.updateSchool(id, request);
		return ApiResponse.<SchoolResponse>builder()
			       .success(true)
			       .message("School Updated Successfully")
			       .data(response)
			       .build();
	}
	
	@DeleteMapping("/{id}")
	public ApiResponse<String> deleteSchool(@PathVariable Long id) {
		schoolService.deleteSchool(id);
		return ApiResponse.<String>builder()
			       .success(true)
			       .message("School deleted successfully")
			       .data("Deleted school with id: " + id)
			       .statusCode(200)
			       .build();
	}
	
	@GetMapping("/{id}")
	public ApiResponse<SchoolResponse> getSchool(@PathVariable Long id) {
		SchoolResponse response = schoolService.getSchool(id);
		return ApiResponse.<SchoolResponse>builder()
			       .success(true)
			       .message("School by ID fetched successfully")
			       .data(response)
			       .build();
	}
	
	@GetMapping("/{schoolCode}")
	public ApiResponse<SchoolResponse> getSchoolByCode(String schoolCode) {
		if (!schoolService.existsByCode(schoolCode))
			throw new ResourceNotFoundException("School with code " + schoolCode + " not found");
		SchoolResponse response = schoolService.getSchoolByCode(schoolCode);
		return ApiResponse.<SchoolResponse>builder()
			       .success(true)
			       .message("School by SchoolCode fetched successfully")
			       .data(response)
			       .build();
	}
	
	@GetMapping("/{email}")
	ApiResponse<SchoolResponse> getSchoolByEmail(String email) {
		if (!schoolService.existsByEmail(email))
			throw new ResourceNotFoundException("School with email " + email + " not found");
		SchoolResponse response = schoolService.getSchoolByEmail(email);
		return ApiResponse.<SchoolResponse>builder()
			       .success(true)
			       .message("School by Email fetched successfully")
			       .data(response)
			       .build();
	}
	
	@GetMapping("/{phone}")
	ApiResponse<SchoolResponse> getSchoolByPhone(String phone) {
		SchoolResponse response = schoolService.getSchoolByPhone(phone);
		return ApiResponse.<SchoolResponse>builder()
			       .success(true)
			       .message("School by Phone fetched successfully")
			       .data(response)
			       .build();
	}
	
	@GetMapping
	public ApiResponse<PageResponse<SchoolResponse>> getAllSchools(Pageable pageable) {
		PageResponse<SchoolResponse> response = schoolService.getAllSchools(pageable);
		return ApiResponse.<PageResponse<SchoolResponse>>builder()
			       .success(true)
			       .message("Schools fetched successfully")
			       .data(response)
			       .build();
	}
	
	@GetMapping("/{status}")
	public ApiResponse<PageResponse<SchoolResponse>> getSchoolsByStatus(@PathVariable SchoolStatus status,
		Pageable pageable) {
		PageResponse<SchoolResponse> response =
			schoolService.getSchoolsByStatus(status, pageable);
		return ApiResponse.<PageResponse<SchoolResponse>>builder()
			       .data(response)
			       .success(true)
			       .message("School by Status fetched Successfully")
			       .build();
	}
	
	@GetMapping("/{name}")
	public ApiResponse<PageResponse<SchoolResponse>> searchSchoolsByName(String name, Pageable pageable) {
		PageResponse<SchoolResponse> response = schoolService.searchSchoolsByName(name, pageable);
		return ApiResponse.<PageResponse<SchoolResponse>>builder()
			       .data(response)
			       .success(true)
			       .message("School by Name fetched Successfully")
			       .build();
	}
	
	@PutMapping("/{id}/status")
	public ApiResponse<SchoolResponse> updateSchoolStatus(Long schoolId, SchoolStatus status) {
		SchoolResponse response = schoolService.updateStatus(schoolId, status);
		return ApiResponse.<SchoolResponse>builder()
			       .data(response)
			       .success(true)
			       .message("School Status Updated Successfully")
			       .build();
	}
}