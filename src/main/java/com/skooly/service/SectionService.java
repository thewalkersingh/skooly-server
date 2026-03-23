package com.skooly.service;
import com.skooly.dto.request.SectionRequest;
import com.skooly.dto.response.SectionResponse;

import java.util.List;

public interface SectionService {
	List<SectionResponse> getSectionsByClass(Long schoolId, Long classId);
	List<SectionResponse> getAllSections(Long schoolId);
	SectionResponse createSection(Long schoolId, SectionRequest req);
	void deleteSection(Long schoolId, Long sectionId);
}
