package com.skooly.mapper;

import com.skooly.dto.request.ClassroomRequest;
import com.skooly.dto.response.ClassroomResponse;
import com.skooly.entity.Classroom;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClassroomMapper {
	
	Classroom toEntity(ClassroomRequest request);
	
	ClassroomResponse toResponse(Classroom classroom);
	
}