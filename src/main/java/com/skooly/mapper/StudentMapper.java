package com.skooly.mapper;
import com.skooly.dto.request.CreateStudentRequest;
import com.skooly.dto.response.StudentResponse;
import com.skooly.dto.response.StudentSummaryResponse;
import com.skooly.model.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StudentMapper {
	@Mapping(target = "classId", source = "schoolClass.id")
	@Mapping(target = "className", source = "schoolClass.name")
	@Mapping(target = "sectionId", source = "section.id")
	@Mapping(target = "sectionName", source = "section.name")
	@Mapping(target = "parentId", source = "parent.id")
	@Mapping(target = "gender", expression = "java(student.getGender() != null ? student.getGender().name() : null)")
	@Mapping(target = "status", expression = "java(student.getStatus() != null ? student.getStatus().name() : null)")
	StudentResponse toResponse(Student student);
	
	@Mapping(target = "className", source = "schoolClass.name")
	@Mapping(target = "sectionName", source = "section.name")
	@Mapping(target = "gender", expression = "java(student.getGender() != null ? student.getGender().name() : null)")
	@Mapping(target = "status", expression = "java(student.getStatus() != null ? student.getStatus().name() : null)")
	StudentSummaryResponse toSummaryResponse(Student student);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "schoolClass", ignore = true)
	@Mapping(target = "section", ignore = true)
	@Mapping(target = "parent", ignore = true)
	@Mapping(target = "user", ignore = true)
	@Mapping(target = "photo", ignore = true)
	@Mapping(target = "status", ignore = true)
	Student toEntity(CreateStudentRequest request);
}