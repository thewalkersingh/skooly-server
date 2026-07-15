package com.skooly.service.impl;

import com.skooly.dto.common.StudentSummary;
import com.skooly.dto.request.ParentRequest;
import com.skooly.dto.response.ParentResponse;
import com.skooly.entity.Parent;
import com.skooly.enums.ParentStatus;
import com.skooly.exception.BadRequestException;
import com.skooly.exception.ResourceNotFoundException;
import com.skooly.mapper.ParentMapper;
import com.skooly.repository.ParentRepository;
import com.skooly.repository.SchoolRepository;
import com.skooly.repository.StudentRepository;
import com.skooly.service.ParentService;
import com.skooly.wrapper.PageResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ParentServiceImpl implements ParentService {
	
	private final ParentRepository parentRepository;
	private final ParentMapper parentMapper;
	private final StudentRepository studentRepository;
	private final SchoolRepository schoolRepository;
	
	// ── Create / Update / Delete ──────────────────────────────────────────────
	public ParentResponse createParent(ParentRequest request) {
		
		if (parentRepository.existsByIdentityPhone(request.getIdentity()
		                                                  .getPhone())) {
			throw new IllegalStateException("Phone already registered");
		}
		if (request.getIdentity()
		           .getEmail() != null && parentRepository.existsByIdentityEmail(request.getIdentity()
		                                                                                .getEmail())) {
			throw new IllegalStateException("Email already registered");
		}
		Parent parent = parentMapper.toEntity(request);
		parent = parentRepository.save(parent);
		return parentMapper.toResponse(parent);
	}
	
	public ParentResponse updateParent(Long parentId, ParentRequest request) {
		
		parentRepository.findById(parentId)
		                .orElseThrow(() -> new ResourceNotFoundException("Parent not found"));
		Parent parent = parentMapper.toEntity(request);
		parent = parentRepository.save(parent);
		return parentMapper.toResponse(parent);
	}
	
	public void deleteParent(Long parentId) {
		
		Parent parent = parentRepository.findById(parentId)
		                                .orElseThrow(() -> new ResourceNotFoundException("Parent not found"));
		parent.setParentStatus(ParentStatus.DELETED);
		parentRepository.save(parent);
	}
	
	public ParentResponse updateStatus(Long parentId, ParentStatus parentStatus) {
		
		Parent parent = parentRepository.findById(parentId)
		                                .orElseThrow(() -> new ResourceNotFoundException("Parent not found"));
		parent.setParentStatus(parentStatus);
		parentRepository.save(parent);
		return parentMapper.toResponse(parent);
	}
	
	// ── Single fetch ──────────────────────────────────────────────────────────
	public ParentResponse getParent(Long parentId) {
		
		Parent parent = parentRepository.findById(parentId)
		                                .orElseThrow(() -> new ResourceNotFoundException("Parent not found"));
		ParentResponse response = parentMapper.toResponse(parent);
		// Populate students manually
		List<StudentSummary> students = studentRepository.findByParentId(parentId)
		                                                 .stream()
		                                                 .map(parentMapper::toStudentSummary)
		                                                 .toList();
		response.setStudents(students);
		return response;
	}
	
	public ParentResponse getParentByPhone(String phone) {
		
		if (!parentRepository.existsByIdentityPhone(phone)) {
			throw new BadRequestException("Phone is invalid");
		}
		Parent parent = parentRepository.findByIdentityPhone(phone)
		                                .orElseThrow(() -> new ResourceNotFoundException("Parent not found"));
		return parentMapper.toResponse(parent);
	}
	
	public ParentResponse getParentByEmail(String email) {
		
		if (!parentRepository.existsByIdentityEmail(email)) {
			throw new BadRequestException("Email is invalid");
		}
		Parent parent = parentRepository.findByIdentityEmail(email)
		                                .orElseThrow(() -> new ResourceNotFoundException("Parent not found"));
		return parentMapper.toResponse(parent);
	}
	
	public ParentResponse getParentWithIdentity(Long parentId) {
		
		Parent parent = parentRepository.findByIdWithIdentity(parentId)
		                                .orElseThrow(() -> new ResourceNotFoundException("Parent not found"));
		return parentMapper.toResponse(parent);
	}
	
	// ── Lists ─────────────────────────────────────────────────────────────────
	public PageResponse<ParentResponse> getAllParents(Pageable pageable) {
		
		Page<Parent> page = parentRepository.findAll(pageable);
		return PageResponse.<ParentResponse>builder()
		                   .data(page.getContent()
		                             .stream()
		                             .map(parentMapper::toResponse)
		                             .toList())
		                   .page(page.getNumber())
		                   .size(page.getSize())
		                   .totalElements(page.getTotalElements())
		                   .totalPages(page.getTotalPages())
		                   .hasNext(page.hasNext())
		                   .hasPrevious(page.hasPrevious())
		                   .build();
	}
	
	// Get all children of a parent
	public List<StudentSummary> getStudentsByParent(Long parentId) {
		
		if (!parentRepository.existsById(parentId)) {
			throw new ResourceNotFoundException("Parent not found");
		}
		
		return studentRepository.findByParentId(parentId)
		                        .stream()
		                        .map(parentMapper::toStudentSummary)
		                        .toList();
	}
	
	// All parents who have at least one child in the given school
	public PageResponse<ParentResponse> getParentsBySchool(Long schoolId, Pageable pageable) {
		
		if (!schoolRepository.existsById(schoolId)) {
			throw new ResourceNotFoundException("School not found");
		}
		Page<Parent> page = parentRepository.findBySchoolId(schoolId, pageable);
		List<ParentResponse> responses = page.getContent()
		                                     .stream()
		                                     .map(parentMapper::toResponse)
		                                     .toList();
		return PageResponse.<ParentResponse>builder()
		                   .data(responses)
		                   .page(page.getNumber())
		                   .size(page.getSize())
		                   .totalElements(page.getTotalElements())
		                   .totalPages(page.getTotalPages())
		                   .hasNext(page.hasNext())
		                   .hasPrevious(page.hasPrevious())
		                   .build();
		
	}
	
	// Parents with more than one child enrolled in the school
	public List<ParentResponse> getParentsWithMultipleChildren(Long schoolId) {
		
		if (!schoolRepository.existsById(schoolId)) {
			throw new ResourceNotFoundException("School not found");
		}
		return parentRepository.findParentsWithMultipleChildrenBySchoolId(schoolId)
		                       .stream()
		                       .map(parentMapper::toResponse)
		                       .toList();
	}
	
	// ── Search ────────────────────────────────────────────────────────────────
	public PageResponse<ParentResponse> searchParentsByName(String name, Pageable pageable) {
		
		Page<Parent> page = parentRepository.searchByName(name, pageable);
		List<ParentResponse> responses = page.getContent()
		                                     .stream()
		                                     .map(parentMapper::toResponse)
		                                     .toList();
		return PageResponse.<ParentResponse>builder()
		                   .data(responses)
		                   .page(page.getNumber())
		                   .size(page.getSize())
		                   .totalElements(page.getTotalElements())
		                   .totalPages(page.getTotalPages())
		                   .hasNext(page.hasNext())
		                   .hasPrevious(page.hasPrevious())
		                   .build();
	}
	
}