package com.skooly.mapper;

import com.skooly.dto.request.ParentRequest;
import com.skooly.dto.response.ParentResponse;
import com.skooly.entity.Parent;
import org.mapstruct.Mapper;

@Mapper(
	 componentModel = "spring",
	 uses = {UserIdentityMapper.class, AddressMapper.class, StudentMapper.class})
public interface ParentMapper {
	
	Parent toEntity(ParentRequest request);
	
	ParentResponse toResponse(Parent parent);
	
}
/*
Embedded Address → handled by AddressMapper.

Nested Identity → handled by UserIdentityMapper.

Children (Students) → handled by StudentMapper. MapStruct automatically maps lists (List<Student> ↔
List<StudentResponse>).
 */