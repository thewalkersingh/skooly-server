package com.skooly.service;
import com.skooly.dto.common.PageResponse;
import com.skooly.dto.request.CreateSectionRequest;
import com.skooly.dto.response.SectionResponse;

public interface SectionService {
	PageResponse<SectionResponse> getAllSections(int page, int size, Long classId);
	
	SectionResponse getSectionById(Long id);
	
	SectionResponse createSection(CreateSectionRequest request);
	
	SectionResponse updateSection(Long id, CreateSectionRequest request);
	
	void deleteSection(Long id);
	
	SectionResponse assignTeacher(Long sectionId, Long teacherId);
}