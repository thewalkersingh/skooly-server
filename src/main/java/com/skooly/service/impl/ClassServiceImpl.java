package com.skooly.service.impl;
import com.skooly.dto.common.PageResponse;
import com.skooly.dto.request.CreateClassRequest;
import com.skooly.dto.response.ClassResponse;
import com.skooly.dto.response.SectionResponse;
import com.skooly.exception.BadRequestException;
import com.skooly.exception.ResourceNotFoundException;
import com.skooly.mapper.ClassMapper;
import com.skooly.mapper.SectionMapper;
import com.skooly.model.SchoolClass;
import com.skooly.repository.SchoolClassRepository;
import com.skooly.repository.SectionRepository;
import com.skooly.service.ClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClassServiceImpl implements ClassService {
	private final SchoolClassRepository classRepository;
	private final SectionRepository sectionRepository;
	private final ClassMapper classMapper;
	private final SectionMapper sectionMapper;
	
	@Override
	@Transactional(readOnly = true)
	public PageResponse<ClassResponse> getAllClasses(int page, int size, String search) {
		Pageable pageable = PageRequest.of(page-1, size, Sort.by("gradeLevel").ascending());
		Page<SchoolClass> classes = classRepository.findWithFilters(search, pageable);
		List<ClassResponse> data = classes.getContent().stream().map(classMapper::toResponse).toList();
		return new PageResponse<>(data, page, size, classes.getTotalElements(), classes.getTotalPages());
	}
	
	@Override
	@Transactional(readOnly = true)
	public ClassResponse getClassById(Long id) {
		return classMapper.toResponse(findClassById(id));
	}
	
	@Override
	public ClassResponse createClass(CreateClassRequest request) {
		if(classRepository.existsByName(request.getName())){
			throw new BadRequestException("Class with name '"+request.getName()+"' already exists");
		}
		return classMapper.toResponse(classRepository.save(classMapper.toEntity(request)));
	}
	
	@Override
	public ClassResponse updateClass(Long id, CreateClassRequest request) {
		SchoolClass schoolClass = findClassById(id);
		schoolClass.setName(request.getName());
		schoolClass.setGradeLevel(request.getGradeLevel());
		return classMapper.toResponse(classRepository.save(schoolClass));
	}
	
	@Override
	public void deleteClass(Long id) {
		if(!classRepository.existsById(id)){
			throw new ResourceNotFoundException("Class not found with id: "+id);
		}
		classRepository.deleteById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public PageResponse<SectionResponse> getSectionsByClass(Long classId, int page, int size) {
		Pageable pageable = PageRequest.of(page-1, size);
		Page<com.skooly.model.Section> sections = sectionRepository.findBySchoolClassId(classId, pageable);
		List<SectionResponse> data = sections.getContent().stream().map(sectionMapper::toResponse).toList();
		return new PageResponse<>(data, page, size, sections.getTotalElements(), sections.getTotalPages());
	}
	
	private SchoolClass findClassById(Long id) {
		return classRepository.findById(id)
				       .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: "+id));
	}
}