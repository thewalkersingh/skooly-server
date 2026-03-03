package com.skooly.service;
import com.skooly.dto.common.PageResponse;
import com.skooly.dto.request.CreateClassRequest;
import com.skooly.dto.response.ClassResponse;
import com.skooly.dto.response.SectionResponse;

public interface ClassService {
	PageResponse<ClassResponse> getAllClasses(int page, int size, String search);
	
	ClassResponse getClassById(Long id);
	
	ClassResponse createClass(CreateClassRequest request);
	
	ClassResponse updateClass(Long id, CreateClassRequest request);
	
	void deleteClass(Long id);
	
	PageResponse<SectionResponse> getSectionsByClass(Long classId, int page, int size);
}