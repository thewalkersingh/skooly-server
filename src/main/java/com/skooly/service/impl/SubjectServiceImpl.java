package com.skooly.service.impl;
import com.skooly.dto.common.PageResponse;
import com.skooly.dto.request.CreateSubjectRequest;
import com.skooly.dto.response.SubjectResponse;
import com.skooly.exception.BadRequestException;
import com.skooly.exception.ResourceNotFoundException;
import com.skooly.mapper.SubjectMapper;
import com.skooly.model.Subject;
import com.skooly.repository.SubjectRepository;
import com.skooly.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SubjectServiceImpl implements SubjectService {
	private final SubjectRepository subjectRepository;
	private final SubjectMapper subjectMapper;
	
	@Override
	@Transactional(readOnly = true)
	public PageResponse<SubjectResponse> getAllSubjects(int page, int size, String search) {
		Pageable pageable = PageRequest.of(page-1, size, Sort.by("name").ascending());
		Page<Subject> subjects = subjectRepository.findWithFilters(search, pageable);
		List<SubjectResponse> data = subjects.getContent().stream().map(subjectMapper::toResponse).toList();
		return new PageResponse<>(data, page, size, subjects.getTotalElements(), subjects.getTotalPages());
	}
	
	@Override
	@Transactional(readOnly = true)
	public SubjectResponse getSubjectById(Long id) {
		return subjectMapper.toResponse(findSubjectById(id));
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<SubjectResponse> getSubjectsByClass(Long classId) {
		return subjectRepository.findByTeachersSchoolClassId(classId)
				       .stream().map(subjectMapper::toResponse).toList();
	}
	
	@Override
	public SubjectResponse createSubject(CreateSubjectRequest request) {
		if(request.getCode() != null && subjectRepository.existsByCode(request.getCode())){
			throw new BadRequestException("Subject code '"+request.getCode()+"' already in use");
		}
		return subjectMapper.toResponse(subjectRepository.save(subjectMapper.toEntity(request)));
	}
	
	@Override
	public SubjectResponse updateSubject(Long id, CreateSubjectRequest request) {
		Subject subject = findSubjectById(id);
		subject.setName(request.getName());
		if(request.getCode() != null)
			subject.setCode(request.getCode());
		if(request.getDescription() != null)
			subject.setDescription(request.getDescription());
		return subjectMapper.toResponse(subjectRepository.save(subject));
	}
	
	@Override
	public void deleteSubject(Long id) {
		if(!subjectRepository.existsById(id)){
			throw new ResourceNotFoundException("Subject not found with id: "+id);
		}
		subjectRepository.deleteById(id);
	}
	
	private Subject findSubjectById(Long id) {
		return subjectRepository.findById(id)
				       .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: "+id));
	}
}