package com.skooly.service.impl;

import com.skooly.dto.request.SchoolRequest;
import com.skooly.dto.response.SchoolResponse;
import com.skooly.entity.School;
import com.skooly.enums.SchoolStatus;
import com.skooly.exception.ResourceNotFoundException;
import com.skooly.mapper.SchoolMapper;
import com.skooly.repository.SchoolRepository;
import com.skooly.service.SchoolService;
import com.skooly.wrapper.PageResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class SchoolServiceImpl implements SchoolService {
	
	private final SchoolRepository schoolRepository;
	private final SchoolMapper schoolMapper;
	
	@Override
	public SchoolResponse createSchool(SchoolRequest request) {
		
		School school = schoolMapper.toEntity(request);
		school = schoolRepository.save(school);
		return schoolMapper.toResponse(school);
	}
	
	public SchoolResponse updateSchool(Long schoolId, SchoolRequest request) {
		
		schoolRepository.findById(schoolId)
		                .orElseThrow(() -> new ResourceNotFoundException("School not found"));
		School school = schoolMapper.toEntity(request);
		School response = schoolRepository.save(school);
		return schoolMapper.toResponse(response);
	}
	
	public void deleteSchool(Long schoolId) {
		
		schoolRepository.findById(schoolId)
		                .ifPresent(school -> school.setSchoolStatus(SchoolStatus.DELETED));
	}
	
	@Override
	public SchoolResponse getSchool(Long id) {
		
		return schoolRepository.findById(id)
		                       .map(schoolMapper::toResponse)
		                       .orElseThrow(() -> new RuntimeException("School not found"));
	}
	
	public SchoolResponse getSchoolByCode(String schoolCode) {
		
		Optional<School> school = schoolRepository.findBySchoolCode(schoolCode);
		return school
			       .map(schoolMapper::toResponse)
			       .orElseThrow(() -> new ResourceNotFoundException("School not found"));
	}
	
	public SchoolResponse getSchoolByEmail(String email) {
		
		Optional<School> schoolByEmail = schoolRepository.findByEmail(email);
		return schoolByEmail
			       .map(schoolMapper::toResponse)
			       .orElseThrow(() -> new ResourceNotFoundException("School not found"));
	}
	
	public SchoolResponse getSchoolByPhone(String phone) {
		
		Optional<School> schoolByPhone = schoolRepository.findByPhone(phone);
		return schoolByPhone.map(schoolMapper::toResponse)
		                    .orElseThrow(() -> new ResourceNotFoundException("School not found"));
	}
	
	public PageResponse<SchoolResponse> getPublicSchools(Pageable pageable) {
		
		Page<School> page = schoolRepository.findBySchoolStatus(SchoolStatus.ACTIVE, pageable);
		List<SchoolResponse> data = page.getContent().stream().map(schoolMapper::toResponse).toList();
		return PageResponse.<SchoolResponse>builder()
		                   .data(data)
		                   .page(page.getNumber())
		                   .size(page.getSize())
		                   .totalElements(page.getTotalElements())
		                   .totalPages(page.getTotalPages())
		                   .hasNext(page.hasNext())
		                   .hasPrevious(page.hasPrevious())
		                   .build();
	}
	
	public PageResponse<SchoolResponse> getAllSchools(Pageable pageable) {
		
		Page<School> page = schoolRepository.findAll(pageable);
		List<SchoolResponse> data = page.getContent().stream().map(schoolMapper::toResponse).toList();
		return PageResponse.<SchoolResponse>builder()
		                   .data(data)
		                   .page(page.getNumber())
		                   .size(page.getSize())
		                   .totalElements(page.getTotalElements())
		                   .totalPages(page.getTotalPages())
		                   .hasNext(page.hasNext())
		                   .hasPrevious(page.hasPrevious())
		                   .build();
	}
	
	public PageResponse<SchoolResponse> getSchoolsBySchoolStatus(SchoolStatus schoolStatus, Pageable pageable) {
		
		Page<School> page = schoolRepository.findBySchoolStatus(schoolStatus, pageable);
		List<SchoolResponse> data = page.getContent().stream().map(schoolMapper::toResponse).toList();
		return PageResponse.<SchoolResponse>builder()
		                   .data(data)
		                   .page(page.getNumber())
		                   .size(page.getSize())
		                   .totalElements(page.getTotalElements())
		                   .totalPages(page.getTotalPages())
		                   .hasNext(page.hasNext())
		                   .hasPrevious(page.hasPrevious())
		                   .build();
	}
	
	public PageResponse<SchoolResponse> searchSchoolsByName(String name, Pageable pageable) {
		
		Page<School> page = schoolRepository.findBySchoolNameContainingIgnoreCase(name, pageable);
		List<SchoolResponse> data = page.getContent().stream().map(schoolMapper::toResponse).toList();
		return PageResponse.<SchoolResponse>builder()
		                   .data(data)
		                   .page(page.getNumber())
		                   .size(page.getSize())
		                   .totalElements(page.getTotalElements())
		                   .totalPages(page.getTotalPages())
		                   .hasNext(page.hasNext())
		                   .hasPrevious(page.hasPrevious())
		                   .build();
	}
	
	public SchoolResponse updateStatus(Long schoolId, SchoolStatus status) {
		
		School school = schoolRepository.findById(schoolId)
		                                .orElseThrow(() -> new ResourceNotFoundException("School not found"));
		school.setSchoolStatus(status);
		School savedSchool = schoolRepository.save(school);
		return schoolMapper.toResponse(savedSchool);
	}
	
	public boolean existsByCode(String schoolCode) {
		
		return schoolRepository.existsBySchoolCode(schoolCode);
	}
	
	public boolean existsByEmail(String email) {
		
		return schoolRepository.existsByEmail(email);
	}
	
}