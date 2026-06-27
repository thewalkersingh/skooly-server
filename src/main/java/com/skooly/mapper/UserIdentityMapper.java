package com.skooly.mapper;

import com.skooly.dto.request.UserIdentityRequest;
import com.skooly.dto.response.UserIdentityResponse;
import com.skooly.entity.UserIdentity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserIdentityMapper {
	
	UserIdentityMapper INSTANCE = Mappers.getMapper(UserIdentityMapper.class);
	
	// Request → Entity
	UserIdentity toEntity(UserIdentityRequest request);
	
	// Entity → Response
	UserIdentityResponse toResponse(UserIdentity entity);
	
}
/*
🔑 How this works
toEntity() → takes a UserIdentityRequest (from client) and builds a UserIdentity entity ready for persistence.

toResponse() → takes a UserIdentity entity (from DB) and converts it into a clean UserIdentityResponse for API output.

componentModel = "spring" → lets you inject the mapper as a Spring bean (@Autowired or constructor injection).

Mappers.getMapper(...) → useful if you want to use it statically without Spring.*/