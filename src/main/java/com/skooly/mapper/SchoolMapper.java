package com.skooly.mapper;

import com.skooly.dto.request.SchoolRequest;
import com.skooly.dto.response.SchoolResponse;
import com.skooly.entity.School;
import org.mapstruct.Mapper;

@Mapper(
	 componentModel = "spring",
	 uses = {TeacherMapper.class, ClassroomMapper.class})
public interface SchoolMapper {
	
	School toEntity(SchoolRequest request);
	
	SchoolResponse toResponse(School school);
	
}