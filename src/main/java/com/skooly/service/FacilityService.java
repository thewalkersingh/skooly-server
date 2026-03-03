package com.skooly.service;
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

public interface FacilityService {
	// Rooms
	PageResponse<RoomResponse> getAllRooms(int page, int size, String search,
			Room.RoomType type, Room.Status status);
	
	RoomResponse getRoomById(Long id);
	
	RoomResponse createRoom(CreateRoomRequest request);
	
	RoomResponse updateRoom(Long id, CreateRoomRequest request);
	
	void deleteRoom(Long id);
	
	RoomResponse updateRoomStatus(Long id, Room.Status status);
	
	// Facilities
	PageResponse<FacilityResponse> getAllFacilities(int page, int size, String search,
			Facility.FacilityStatus status);
	
	FacilityResponse getFacilityById(Long id);
	
	FacilityResponse createFacility(CreateFacilityRequest request);
	
	FacilityResponse updateFacility(Long id, CreateFacilityRequest request);
	
	void deleteFacility(Long id);
	
	// Maintenance Logs
	PageResponse<MaintenanceLogResponse> getAllMaintenanceLogs(int page, int size,
			Long facilityId,
			MaintenanceLog.MaintenanceStatus status);
	
	MaintenanceLogResponse getMaintenanceLogById(Long id);
	
	MaintenanceLogResponse createMaintenanceLog(CreateMaintenanceLogRequest request, Long reportedByUserId);
	
	MaintenanceLogResponse resolveMaintenanceLog(Long id);
	
	void deleteMaintenanceLog(Long id);
}