package com.skooly.mapper;
import com.skooly.dto.request.CreateSubjectRequest;
import com.skooly.dto.response.SubjectResponse;
import com.skooly.model.Subject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SubjectMapper {
	SubjectResponse toResponse(Subject subject);
	
	@Mapping(target = "id", ignore = true)
	Subject toEntity(CreateSubjectRequest request);
}