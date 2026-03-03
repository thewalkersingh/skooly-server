package com.skooly.service.impl;
import com.skooly.dto.common.PageResponse;
import com.skooly.dto.request.CreateFacilityRequest;
import com.skooly.dto.request.CreateMaintenanceLogRequest;
import com.skooly.dto.request.CreateRoomRequest;
import com.skooly.dto.response.FacilityResponse;
import com.skooly.dto.response.MaintenanceLogResponse;
import com.skooly.dto.response.RoomResponse;
import com.skooly.exception.ResourceNotFoundException;
import com.skooly.mapper.FacilityMapper;
import com.skooly.model.*;
import com.skooly.repository.*;
import com.skooly.service.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FacilityServiceImpl implements FacilityService {
	private final RoomRepository roomRepository;
	private final FacilityRepository facilityRepository;
	private final MaintenanceLogRepository maintenanceRepository;
	private final UserRepository userRepository;
	private final FacilityMapper facilityMapper;
	
	// ── Rooms ────────────────────────────────────────────────────────────────
	
	@Override
	@Transactional(readOnly = true)
	public PageResponse<RoomResponse> getAllRooms(
			int page, int size, String search, Room.RoomType type, Room.Status status) {
		
		Pageable pageable = PageRequest.of(page-1, size, Sort.by("name").ascending());
		Page<Room> rooms = roomRepository.findWithFilters(type, status, search, pageable);
		List<RoomResponse> data = rooms.getContent().stream().map(facilityMapper::toRoomResponse).toList();
		return new PageResponse<>(data, page, size, rooms.getTotalElements(), rooms.getTotalPages());
	}
	
	@Override
	@Transactional(readOnly = true)
	public RoomResponse getRoomById(Long id) {
		return facilityMapper.toRoomResponse(findRoomById(id));
	}
	
	@Override
	public RoomResponse createRoom(CreateRoomRequest request) {
		return facilityMapper.toRoomResponse(roomRepository.save(facilityMapper.toRoomEntity(request)));
	}
	
	@Override
	public RoomResponse updateRoom(Long id, CreateRoomRequest request) {
		Room room = findRoomById(id);
		room.setName(request.getName());
		if(request.getType() != null)
			room.setType(request.getType());
		if(request.getCapacity() != null)
			room.setCapacity(request.getCapacity());
		if(request.getFloor() != null)
			room.setFloor(request.getFloor());
		if(request.getBuilding() != null)
			room.setBuilding(request.getBuilding());
		if(request.getStatus() != null)
			room.setStatus(request.getStatus());
		return facilityMapper.toRoomResponse(roomRepository.save(room));
	}
	
	@Override
	public void deleteRoom(Long id) {
		if(!roomRepository.existsById(id)){
			throw new ResourceNotFoundException("Room not found with id: "+id);
		}
		roomRepository.deleteById(id);
	}
	
	@Override
	public RoomResponse updateRoomStatus(Long id, Room.Status status) {
		Room room = findRoomById(id);
		room.setStatus(status);
		return facilityMapper.toRoomResponse(roomRepository.save(room));
	}
	
	// ── Facilities ───────────────────────────────────────────────────────────
	
	@Override
	@Transactional(readOnly = true)
	public PageResponse<FacilityResponse> getAllFacilities(
			int page, int size, String search, Facility.FacilityStatus status) {
		
		Pageable pageable = PageRequest.of(page-1, size, Sort.by("name").ascending());
		Page<Facility> facilities = facilityRepository.findWithFilters(status, search, pageable);
		List<FacilityResponse> data = facilities.getContent().stream().map(facilityMapper::toFacilityResponse).toList();
		return new PageResponse<>(data, page, size, facilities.getTotalElements(), facilities.getTotalPages());
	}
	
	@Override
	@Transactional(readOnly = true)
	public FacilityResponse getFacilityById(Long id) {
		return facilityMapper.toFacilityResponse(findFacilityById(id));
	}
	
	@Override
	public FacilityResponse createFacility(CreateFacilityRequest request) {
		return facilityMapper.toFacilityResponse(
				facilityRepository.save(facilityMapper.toFacilityEntity(request)));
	}
	
	@Override
	public FacilityResponse updateFacility(Long id, CreateFacilityRequest request) {
		Facility facility = findFacilityById(id);
		facility.setName(request.getName());
		if(request.getDescription() != null)
			facility.setDescription(request.getDescription());
		if(request.getLocation() != null)
			facility.setLocation(request.getLocation());
		if(request.getStatus() != null)
			facility.setStatus(request.getStatus());
		return facilityMapper.toFacilityResponse(facilityRepository.save(facility));
	}
	
	@Override
	public void deleteFacility(Long id) {
		if(!facilityRepository.existsById(id)){
			throw new ResourceNotFoundException("Facility not found with id: "+id);
		}
		facilityRepository.deleteById(id);
	}
	
	// ── Maintenance Logs ─────────────────────────────────────────────────────
	
	@Override
	@Transactional(readOnly = true)
	public PageResponse<MaintenanceLogResponse> getAllMaintenanceLogs(
			int page, int size, Long facilityId, MaintenanceLog.MaintenanceStatus status) {
		
		Pageable pageable = PageRequest.of(page-1, size, Sort.by("reportedDate").descending());
		Page<MaintenanceLog> logs;
		
		if(facilityId != null){
			logs = maintenanceRepository.findByFacilityId(facilityId, pageable);
		} else if(status != null){
			logs = maintenanceRepository.findByStatus(status, pageable);
		} else{
			logs = maintenanceRepository.findAll(pageable);
		}
		
		List<MaintenanceLogResponse> data = logs.getContent().stream()
				                                    .map(facilityMapper::toMaintenanceResponse).toList();
		return new PageResponse<>(data, page, size, logs.getTotalElements(), logs.getTotalPages());
	}
	
	@Override
	@Transactional(readOnly = true)
	public MaintenanceLogResponse getMaintenanceLogById(Long id) {
		return facilityMapper.toMaintenanceResponse(findMaintenanceById(id));
	}
	
	@Override
	public MaintenanceLogResponse createMaintenanceLog(
			CreateMaintenanceLogRequest request, Long reportedByUserId) {
		
		Facility facility = findFacilityById(request.getFacilityId());
		User reportedBy = userRepository.findById(reportedByUserId)
				                  .orElseThrow(() -> new ResourceNotFoundException("User not found"));
		
		MaintenanceLog log = MaintenanceLog.builder()
				                     .facility(facility)
				                     .reportedBy(reportedBy)
				                     .issue(request.getIssue())
				                     .reportedDate(request.getReportedDate())
				                     .status(MaintenanceLog.MaintenanceStatus.OPEN)
				                     .build();
		
		return facilityMapper.toMaintenanceResponse(maintenanceRepository.save(log));
	}
	
	@Override
	public MaintenanceLogResponse resolveMaintenanceLog(Long id) {
		MaintenanceLog log = findMaintenanceById(id);
		log.setStatus(MaintenanceLog.MaintenanceStatus.RESOLVED);
		log.setResolvedDate(LocalDate.now());
		return facilityMapper.toMaintenanceResponse(maintenanceRepository.save(log));
	}
	
	@Override
	public void deleteMaintenanceLog(Long id) {
		if(!maintenanceRepository.existsById(id)){
			throw new ResourceNotFoundException("Maintenance log not found with id: "+id);
		}
		maintenanceRepository.deleteById(id);
	}
	
	// ── Private helpers ──────────────────────────────────────────────────────
	
	private Room findRoomById(Long id) {
		return roomRepository.findById(id)
				       .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: "+id));
	}
	
	private Facility findFacilityById(Long id) {
		return facilityRepository.findById(id)
				       .orElseThrow(() -> new ResourceNotFoundException("Facility not found with id: "+id));
	}
	
	private MaintenanceLog findMaintenanceById(Long id) {
		return maintenanceRepository.findById(id)
				       .orElseThrow(() -> new ResourceNotFoundException("Maintenance log not found with id: "+id));
	}
}