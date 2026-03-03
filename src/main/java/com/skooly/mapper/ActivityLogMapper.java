package com.skooly.mapper;
import com.skooly.dto.response.ActivityLogResponse;
import com.skooly.model.ActivityLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ActivityLogMapper {
	@Mapping(target = "userId", source = "user.id")
	@Mapping(target = "username", expression = "java(a.getUser() != null ? a.getUser().getUsername() : 'system')")
	@Mapping(target = "action", expression = "java(a.getAction().name())")
	ActivityLogResponse toResponse(ActivityLog a);
}