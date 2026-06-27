package com.skooly.mapper;

import com.skooly.dto.request.AddressRequest;
import com.skooly.dto.response.AddressResponse;
import com.skooly.entity.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
	
	Address toEntity(AddressRequest request);
	
	AddressResponse toResponse(Address entity);
	
}