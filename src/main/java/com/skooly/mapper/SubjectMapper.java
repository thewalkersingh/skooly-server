package com.skooly.mapper;

import com.skooly.dto.common.TeacherSummary;
import com.skooly.dto.request.SubjectRequest;
import com.skooly.dto.response.SubjectResponse;
import com.skooly.entity.Subject;
import com.skooly.entity.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubjectMapper {
	
	@Mapping(target = "teachers", source = "teachers")
	SubjectResponse toResponse(Subject subject);
	
	@Mapping(target = "teacherName", expression = "java(teacher.getIdentity().getFirstName() + ' ' + teacher" +
		                                              ".getIdentity().getLastName())")
	@Mapping(target = "phone", source = "identity.phone")
	TeacherSummary toTeacherSummary(Teacher teacher);
	
	@Mapping(target = "teachers", ignore = true)
	Subject toEntity(SubjectRequest request);
	
}