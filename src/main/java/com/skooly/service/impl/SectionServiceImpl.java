package com.skooly.service.impl;
import com.skooly.dto.common.PageResponse;
import com.skooly.dto.request.CreateSectionRequest;
import com.skooly.dto.response.SectionResponse;
import com.skooly.exception.BadRequestException;
import com.skooly.exception.ResourceNotFoundException;
import com.skooly.mapper.SectionMapper;
import com.skooly.model.SchoolClass;
import com.skooly.model.Section;
import com.skooly.model.Teacher;
import com.skooly.repository.SchoolClassRepository;
import com.skooly.repository.SectionRepository;
import com.skooly.repository.TeacherRepository;
import com.skooly.service.SectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SectionServiceImpl implements SectionService {
	private final SectionRepository sectionRepository;
	private final SchoolClassRepository classRepository;
	private final TeacherRepository teacherRepository;
	private final SectionMapper sectionMapper;
	
	@Override
	@Transactional(readOnly = true)
	public PageResponse<SectionResponse> getAllSections(int page, int size, Long classId) {
		Pageable pageable = PageRequest.of(page-1, size, Sort.by("name").ascending());
		Page<Section> sections = classId != null
		                         ? sectionRepository.findBySchoolClassId(classId, pageable)
		                         : sectionRepository.findAll(pageable);
		List<SectionResponse> data = sections.getContent().stream().map(sectionMapper::toResponse).toList();
		return new PageResponse<>(data, page, size, sections.getTotalElements(), sections.getTotalPages());
	}
	
	@Override
	@Transactional(readOnly = true)
	public SectionResponse getSectionById(Long id) {
		return sectionMapper.toResponse(findSectionById(id));
	}
	
	@Override
	public SectionResponse createSection(CreateSectionRequest request) {
		if(sectionRepository.existsByNameAndSchoolClassId(request.getName(), request.getClassId())){
			throw new BadRequestException("Section '"+request.getName()+"' already exists in this class");
		}
		Section section = sectionMapper.toEntity(request);
		section.setSchoolClass(findClassById(request.getClassId()));
		if(request.getTeacherId() != null){
			section.setTeacher(findTeacherById(request.getTeacherId()));
		}
		return sectionMapper.toResponse(sectionRepository.save(section));
	}
	
	@Override
	public SectionResponse updateSection(Long id, CreateSectionRequest request) {
		Section section = findSectionById(id);
		section.setName(request.getName());
		if(request.getClassId() != null)
			section.setSchoolClass(findClassById(request.getClassId()));
		if(request.getTeacherId() != null)
			section.setTeacher(findTeacherById(request.getTeacherId()));
		return sectionMapper.toResponse(sectionRepository.save(section));
	}
	
	@Override
	public void deleteSection(Long id) {
		if(!sectionRepository.existsById(id)){
			throw new ResourceNotFoundException("Section not found with id: "+id);
		}
		sectionRepository.deleteById(id);
	}
	
	@Override
	public SectionResponse assignTeacher(Long sectionId, Long teacherId) {
		Section section = findSectionById(sectionId);
		section.setTeacher(findTeacherById(teacherId));
		return sectionMapper.toResponse(sectionRepository.save(section));
	}
	
	private Section findSectionById(Long id) {
		return sectionRepository.findById(id)
				       .orElseThrow(() -> new ResourceNotFoundException("Section not found with id: "+id));
	}
	
	private SchoolClass findClassById(Long id) {
		return classRepository.findById(id)
				       .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: "+id));
	}
	
	private Teacher findTeacherById(Long id) {
		return teacherRepository.findById(id)
				       .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: "+id));
	}
}