package com.skooly.mapper;
import com.skooly.dto.request.CreateSectionRequest;
import com.skooly.dto.response.SectionResponse;
import com.skooly.model.Section;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SectionMapper {
	@Mapping(target = "classId", source = "schoolClass.id")
	@Mapping(target = "className", source = "schoolClass.name")
	@Mapping(target = "teacherId", source = "teacher.id")
	@Mapping(target = "teacherName",
			expression = "java(section.getTeacher() != null ? section.getTeacher().getFirstName() + ' ' + section" +
			             ".getTeacher().getLastName() : null)")
	SectionResponse toResponse(Section section);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "schoolClass", ignore = true)
	@Mapping(target = "teacher", ignore = true)
	Section toEntity(CreateSectionRequest request);
}