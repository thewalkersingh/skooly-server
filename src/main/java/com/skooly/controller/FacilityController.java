package com.skooly.controller;
import com.skooly.dto.common.ApiResponse;
import com.skooly.dto.common.PageResponse;
import com.skooly.dto.request.CreateFacilityRequest;
import com.skooly.dto.request.CreateMaintenanceLogRequest;
import com.skooly.dto.request.CreateRoomRequest;
import com.skooly.dto.response.FacilityResponse;
import com.skooly.dto.response.MaintenanceLogResponse;
import com.skooly.dto.response.RoomResponse;
import com.skooly.model.Facility;
import com.skooly.model.MaintenanceLog;
import com.skooly.model.Room;
import com.skooly.security.UserPrincipal;
import com.skooly.service.FacilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FacilityController {
	private final FacilityService facilityService;
	
	// ── Rooms ────────────────────────────────────────────────────────────────
	
	@GetMapping("/rooms")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<PageResponse<RoomResponse>>> getAllRooms(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) String search,
			@RequestParam(required = false) Room.RoomType type,
			@RequestParam(required = false) Room.Status status) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Rooms fetched successfully",
		                                           facilityService.getAllRooms(page, size, search, type, status)));
	}
	
	@GetMapping("/rooms/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<RoomResponse>> getRoomById(@PathVariable Long id) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Room fetched successfully",
		                                           facilityService.getRoomById(id)));
	}
	
	@PostMapping("/rooms")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<RoomResponse>> createRoom(
			@Valid @RequestBody CreateRoomRequest request) {
		return ResponseEntity.status(201).body(new ApiResponse<>(true, "Room created successfully",
		                                                         facilityService.createRoom(request)));
	}
	
	@PutMapping("/rooms/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<RoomResponse>> updateRoom(
			@PathVariable Long id, @Valid @RequestBody CreateRoomRequest request) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Room updated successfully",
		                                           facilityService.updateRoom(id, request)));
	}
	
	@DeleteMapping("/rooms/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> deleteRoom(@PathVariable Long id) {
		facilityService.deleteRoom(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Room deleted successfully", null));
	}
	
	@PatchMapping("/rooms/{id}/status")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<RoomResponse>> updateRoomStatus(
			@PathVariable Long id, @RequestParam Room.Status status) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Room status updated",
		                                           facilityService.updateRoomStatus(id, status)));
	}
	
	// ── Facilities ───────────────────────────────────────────────────────────
	
	@GetMapping("/facilities")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<PageResponse<FacilityResponse>>> getAllFacilities(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) String search,
			@RequestParam(required = false) Facility.FacilityStatus status) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Facilities fetched successfully",
		                                           facilityService.getAllFacilities(page, size, search, status)));
	}
	
	@GetMapping("/facilities/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<FacilityResponse>> getFacilityById(@PathVariable Long id) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Facility fetched successfully",
		                                           facilityService.getFacilityById(id)));
	}
	
	@PostMapping("/facilities")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<FacilityResponse>> createFacility(
			@Valid @RequestBody CreateFacilityRequest request) {
		return ResponseEntity.status(201).body(new ApiResponse<>(true, "Facility created successfully",
		                                                         facilityService.createFacility(request)));
	}
	
	@PutMapping("/facilities/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<FacilityResponse>> updateFacility(
			@PathVariable Long id, @Valid @RequestBody CreateFacilityRequest request) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Facility updated successfully",
		                                           facilityService.updateFacility(id, request)));
	}
	
	@DeleteMapping("/facilities/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> deleteFacility(@PathVariable Long id) {
		facilityService.deleteFacility(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Facility deleted successfully", null));
	}
	
	// ── Maintenance Logs ─────────────────────────────────────────────────────
	
	@GetMapping("/maintenance")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<PageResponse<MaintenanceLogResponse>>> getAllMaintenanceLogs(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) Long facilityId,
			@RequestParam(required = false) MaintenanceLog.MaintenanceStatus status) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Maintenance logs fetched successfully",
		                                           facilityService.getAllMaintenanceLogs(page, size, facilityId,
		                                                                                 status)));
	}
	
	@GetMapping("/maintenance/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<MaintenanceLogResponse>> getMaintenanceLogById(@PathVariable Long id) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Maintenance log fetched successfully",
		                                           facilityService.getMaintenanceLogById(id)));
	}
	
	@PostMapping("/maintenance")
	@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	public ResponseEntity<ApiResponse<MaintenanceLogResponse>> createMaintenanceLog(
			@Valid @RequestBody CreateMaintenanceLogRequest request,
			@AuthenticationPrincipal UserPrincipal userPrincipal) {
		return ResponseEntity.status(201).body(new ApiResponse<>(true, "Maintenance log created successfully",
		                                                         facilityService.createMaintenanceLog(request,
		                                                                                              userPrincipal.getId())));
	}
	
	@PatchMapping("/maintenance/{id}/resolve")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<MaintenanceLogResponse>> resolveMaintenanceLog(@PathVariable Long id) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Maintenance log resolved",
		                                           facilityService.resolveMaintenanceLog(id)));
	}
	
	@DeleteMapping("/maintenance/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> deleteMaintenanceLog(@PathVariable Long id) {
		facilityService.deleteMaintenanceLog(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Maintenance log deleted successfully", null));
	}
}