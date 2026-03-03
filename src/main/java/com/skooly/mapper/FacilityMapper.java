package com.skooly.mapper;

import com.skooly.dto.request.CreateFacilityRequest;
import com.skooly.dto.request.CreateRoomRequest;
import com.skooly.dto.response.FacilityResponse;
import com.skooly.dto.response.MaintenanceLogResponse;
import com.skooly.dto.response.RoomResponse;
import com.skooly.model.Facility;
import com.skooly.model.MaintenanceLog;
import com.skooly.model.Room;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FacilityMapper {
	
	@Mapping(target = "type",   expression = "java(r.getType()   != null ? r.getType().name()   : null)")
	@Mapping(target = "status", expression = "java(r.getStatus() != null ? r.getStatus().name() : null)")
	RoomResponse toRoomResponse(Room r);
	
	@Mapping(target = "status", expression = "java(f.getStatus() != null ? f.getStatus().name() : null)")
	FacilityResponse toFacilityResponse(Facility f);
	
	@Mapping(target = "facilityId",   source = "facility.id")
	@Mapping(target = "facilityName", source = "facility.name")
	@Mapping(target = "reportedBy",   expression = "java(m.getReportedBy() != null ? m.getReportedBy().getUsername() : null)")
	@Mapping(target = "status",       expression = "java(m.getStatus().name())")
	MaintenanceLogResponse toMaintenanceResponse(MaintenanceLog m);
	
	@Mapping(target = "id", ignore = true)
	Room toRoomEntity(CreateRoomRequest request);
	
	@Mapping(target = "id", ignore = true)
	Facility toFacilityEntity(CreateFacilityRequest request);
}