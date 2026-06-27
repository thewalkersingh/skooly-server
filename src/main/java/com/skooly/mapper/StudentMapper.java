package com.skooly.mapper;

import com.skooly.dto.ParentSummary;
import com.skooly.dto.request.StudentRequest;
import com.skooly.dto.response.StudentResponse;
import com.skooly.entity.Parent;
import com.skooly.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
	uses = {UserIdentityMapper.class, AddressMapper.class})
public interface StudentMapper {
	
	@Mapping(target = "section", ignore = true)   // set manually in service
	@Mapping(target = "parent", ignore = true)   // set manually in service
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	Student toEntity(StudentRequest request);
	
	@Mapping(target = "sectionId", source = "section.id")
	@Mapping(target = "sectionName", source = "section.sectionName")
	@Mapping(target = "classroomName", source = "section.classroom.classroomName")
	@Mapping(target = "parent", source = "parent")
		// uses toParentSummary below
	StudentResponse toResponse(Student student);
	
	// MapStruct auto-uses for Parent → ParentSummary
	@Mapping(target = "parentName", expression = "java(parent.getIdentity().getFirstName() + ' ' + parent.getIdentity()" +
		                                             ".getLastName())")
	@Mapping(target = "phone", source = "identity.phone")
	@Mapping(target = "relation", source = "relation")
	ParentSummary toParentSummary(Parent parent);
	
}