package com.skooly.mapper;

import com.skooly.dto.common.SubjectSummary;
import com.skooly.dto.request.SectionRequest;
import com.skooly.dto.response.SectionResponse;
import com.skooly.entity.Section;
import com.skooly.entity.Subject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SectionMapper {
	
	@Mapping(target = "classroomId", source = "classroom.id")
	@Mapping(target = "classroomName", source = "classroom.classroomName")
	@Mapping(target = "teacherId", source = "teacher.id")
	@Mapping(target = "teacherName",
		expression = "java(section.getTeacher() != null ? section.getTeacher().getIdentity()" +
			             ".getFirstName() + ' ' + section.getTeacher().getIdentity().getLastName() : null)")
	@Mapping(target = "subjects", source = "subjects")
	SectionResponse toResponse(Section section);
	
	SubjectSummary toSubjectSummary(Subject subject);  // MapStruct auto-uses this for the list
	
	@Mapping(target = "classroom", ignore = true)   // set in service via classroomId path var
	@Mapping(target = "teacher", ignore = true)   // set in service if teacherId present
	@Mapping(target = "subjects", ignore = true)   // set in service if subjectIds present
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	Section toEntity(SectionRequest request);
	
}