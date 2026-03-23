package com.skooly.service.impl;
import com.skooly.dto.request.SchoolClassRequest;
import com.skooly.dto.response.SchoolClassResponse;
import com.skooly.exception.BadRequestException;
import com.skooly.exception.ResourceNotFoundException;
import com.skooly.model.SchoolClass;
import com.skooly.repository.SchoolClassRepository;
import com.skooly.repository.SchoolRepository;
import com.skooly.repository.SectionRepository;
import com.skooly.service.SchoolClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolClassServiceImpl implements SchoolClassService {
	private final SchoolClassRepository classRepository;
	private final SectionRepository sectionRepository;
	private final SchoolRepository schoolRepository;
	
	public List<SchoolClassResponse> getAllClasses(Long schoolId) {
		return classRepository.findBySchoolIdOrderByGradeLevelAsc(schoolId)
				       .stream().map(SchoolClassResponse::from).toList();
	}
	
	public SchoolClassResponse createClass(Long schoolId, SchoolClassRequest req) {
		var school = schoolRepository.findById(schoolId)
				             .orElseThrow(() -> new ResourceNotFoundException("School", schoolId));
		var sc = SchoolClass.builder()
				         .school(school)
				         .name(req.getName())
				         .gradeLevel(req.getGradeLevel())
				         .build();
		return SchoolClassResponse.from(classRepository.save(sc));
	}
	
	public SchoolClassResponse updateClass(Long schoolId, Long classId, SchoolClassRequest req) {
		var sc = classRepository.findById(classId)
				         .orElseThrow(() -> new ResourceNotFoundException("Class", classId));
		if(!sc.getSchool().getId().equals(schoolId))
			throw new BadRequestException("Class does not belong to this school");
		sc.setName(req.getName());
		sc.setGradeLevel(req.getGradeLevel());
		return SchoolClassResponse.from(classRepository.save(sc));
	}
	
	public void deleteClass(Long schoolId, Long classId) {
		var sc = classRepository.findById(classId)
				         .orElseThrow(() -> new ResourceNotFoundException("Class", classId));
		if(!sc.getSchool().getId().equals(schoolId))
			throw new BadRequestException("Class does not belong to this school");
		classRepository.delete(sc);
	}
}