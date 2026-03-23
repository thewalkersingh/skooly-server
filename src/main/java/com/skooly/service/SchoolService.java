package com.skooly.service;
import com.skooly.dto.request.SchoolRequest;
import com.skooly.dto.response.SchoolResponse;

import java.util.List;

public interface SchoolService {
	List<SchoolResponse> getAllSchools();
	
	SchoolResponse getSchoolById(Long id);
	
	SchoolResponse createSchool(SchoolRequest request);
	
	SchoolResponse updateSchool(Long id, SchoolRequest request);
	
	void deleteSchool(Long id);
}
