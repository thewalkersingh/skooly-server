package com.skooly.service.impl;
import com.skooly.dto.request.SectionRequest;
import com.skooly.dto.response.SectionResponse;
import com.skooly.exception.BadRequestException;
import com.skooly.exception.ResourceNotFoundException;
import com.skooly.model.Section;
import com.skooly.repository.SchoolClassRepository;
import com.skooly.repository.SchoolRepository;
import com.skooly.repository.SectionRepository;
import com.skooly.service.SectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SectionsServiceImpl implements SectionService {
	private final SectionRepository sectionRepository;
	private final SchoolRepository schoolRepository;
	private final SchoolClassRepository classRepository;
	
	public List<SectionResponse> getSectionsByClass(Long schoolId, Long classId) {
		return sectionRepository.findBySchoolIdAndSchoolClassId(schoolId, classId)
				       .stream().map(SectionResponse::from).toList();
	}
	
	public List<SectionResponse> getAllSections(Long schoolId) {
		return sectionRepository.findBySchoolId(schoolId)
				       .stream().map(SectionResponse::from).toList();
	}
	
	public SectionResponse createSection(Long schoolId, SectionRequest req) {
		var school = schoolRepository.findById(schoolId)
				             .orElseThrow(() -> new ResourceNotFoundException("School", schoolId));
		var sc = classRepository.findById(req.getClassId())
				         .orElseThrow(() -> new ResourceNotFoundException("Class", req.getClassId()));
		var section = Section.builder()
				              .school(school)
				              .schoolClass(sc)
				              .name(req.getName())
				              .capacity(req.getCapacity() != null ? req.getCapacity() : 40)
				              .build();
		return SectionResponse.from(sectionRepository.save(section));
	}
	
	public void deleteSection(Long schoolId, Long sectionId) {
		var section = sectionRepository.findById(sectionId)
				              .orElseThrow(() -> new ResourceNotFoundException("Section", sectionId));
		if(!section.getSchool().getId().equals(schoolId))
			throw new BadRequestException("Section does not belong to this school");
		sectionRepository.delete(section);
	}
}
