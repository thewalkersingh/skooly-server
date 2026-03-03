package com.skooly.mapper;
import com.skooly.dto.request.CreateParentRequest;
import com.skooly.dto.response.NotificationResponse;
import com.skooly.dto.response.ParentResponse;
import com.skooly.model.Notification;
import com.skooly.model.Parent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ParentMapper {
	@Mapping(target = "userId", source = "user.id")
	@Mapping(target = "relation", expression = "java(p.getRelation().name())")
	ParentResponse toResponse(Parent p);
	
	@Mapping(target = "userId", source = "user.id")
	NotificationResponse toNotificationResponse(Notification n);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "user", ignore = true)
	Parent toEntity(CreateParentRequest request);
}