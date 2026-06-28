package com.skooly.controller;

import com.skooly.dto.request.SchoolRequest;
import com.skooly.dto.response.SchoolResponse;
import com.skooly.enums.SchoolStatus;
import com.skooly.exception.ResourceNotFoundException;
import com.skooly.service.SchoolService;
import com.skooly.wrapper.ApiResponse;
import com.skooly.wrapper.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schools")
@RequiredArgsConstructor
@Tag(name = "School Management", description = "Endpoints for managing schools")
public class SchoolController {
	
	private final SchoolService schoolService;
	
	// ── Create a new school ────────────────────────────────────────────────
	@io.swagger.v3.oas.annotations.Operation(
		summary = "Get all schools",
		description = "Fetches a list of all schools registered in the system."
	)
	@io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200", description = "Schools fetched successfully"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "404", description = "No schools found")
	})
	@PostMapping
	public ApiResponse<SchoolResponse> createSchool(@RequestBody SchoolRequest request) {
		SchoolResponse response = schoolService.createSchool(request);
		return ApiResponse.<SchoolResponse>builder()
		                  .success(true)
		                  .message("School created successfully")
		                  .data(response)
		                  .statusCode(201)
		                  .build();
	}
	
	// ── Update school details ─────────────────────────────────────────────
	@io.swagger.v3.oas.annotations.Operation(summary = "Update school details",
		description = "Updates an existing school's information using its ID.")
	@io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
			description = "School updated successfully"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "School not found")
	})
	@PatchMapping("/{id}/request")
	public ApiResponse<SchoolResponse> updateSchool(@PathVariable Long id, @RequestBody SchoolRequest request) {
		SchoolResponse response = schoolService.updateSchool(id, request);
		return ApiResponse.<SchoolResponse>builder()
		                  .success(true)
		                  .message("School Updated Successfully")
		                  .data(response)
		                  .statusCode(200)
		                  .build();
	}
	
	// ── Delete a school ───────────────────────────────────────────────────
	@io.swagger.v3.oas.annotations.Operation(summary = "Delete a school",
		description = "Deletes a school by its ID. This action is irreversible.")
	@io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "School deleted " +
			                                                                                         "successfully"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "School not found")
	})
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
	
	// ── Get school by ID ──────────────────────────────────────────────────
	@io.swagger.v3.oas.annotations.Operation(summary = "Get school by ID",
		description = "Fetches details of a school using its unique ID.")
	@io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "School fetched " +
			                                                                                         "successfully"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "School not found")
	})
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
	
	// ── Get all schools ───────────────────────────────────────────────────
	@io.swagger.v3.oas.annotations.Operation(summary = "Get all schools",
		description = "Fetches a list of all schools registered in the system.")
	@io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Schools fetched " +
			                                                                                         "successfully")
	})
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