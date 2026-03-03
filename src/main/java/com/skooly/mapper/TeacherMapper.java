// TeacherMapper.java
package com.skooly.mapper;
import com.skooly.dto.request.CreateTeacherRequest;
import com.skooly.dto.response.TeacherResponse;
import com.skooly.dto.response.TeacherSummaryResponse;
import com.skooly.model.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TeacherMapper {
	@Mapping(target = "userId", source = "user.id")
	@Mapping(target = "subjectId", source = "subject.id")
	@Mapping(target = "subjectName", source = "subject.name")
	@Mapping(target = "gender", expression = "java(teacher.getGender() != null ? teacher.getGender().name() : null)")
	@Mapping(target = "status", expression = "java(teacher.getStatus() != null ? teacher.getStatus().name() : null)")
	TeacherResponse toResponse(Teacher teacher);
	
	@Mapping(target = "subjectName", source = "subject.name")
	@Mapping(target = "gender", expression = "java(teacher.getGender() != null ? teacher.getGender().name() : null)")
	@Mapping(target = "status", expression = "java(teacher.getStatus() != null ? teacher.getStatus().name() : null)")
	TeacherSummaryResponse toSummaryResponse(Teacher teacher);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "user", ignore = true)
	@Mapping(target = "subject", ignore = true)
	@Mapping(target = "photo", ignore = true)
	@Mapping(target = "status", ignore = true)
	Teacher toEntity(CreateTeacherRequest request);
}