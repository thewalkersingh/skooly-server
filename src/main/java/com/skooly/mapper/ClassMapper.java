package com.skooly.mapper;
import com.skooly.dto.request.CreateClassRequest;
import com.skooly.dto.response.ClassResponse;
import com.skooly.model.SchoolClass;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClassMapper {
	ClassResponse toResponse(SchoolClass schoolClass);
	
	@Mapping(target = "id", ignore = true)
	SchoolClass toEntity(CreateClassRequest request);
}