package com.skooly.service;
import com.skooly.dto.request.SchoolClassRequest;
import com.skooly.dto.response.SchoolClassResponse;

import java.util.List;

public interface SchoolClassService {
	List<SchoolClassResponse> getAllClasses(Long schoolId);
	
	SchoolClassResponse createClass(Long schoolId, SchoolClassRequest req);
	
	SchoolClassResponse updateClass(Long schoolId, Long classId, SchoolClassRequest req);
	void deleteClass(Long schoolId, Long classId);
}
