package com.skooly.service.impl;

import com.skooly.dto.request.ParentRequest;
import com.skooly.dto.response.ParentResponse;
import com.skooly.entity.Parent;
import com.skooly.entity.Student;
import com.skooly.mapper.ParentMapper;
import com.skooly.mapper.StudentMapper;
import com.skooly.repository.ParentRepository;
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
	private final StudentMapper studentMapper;
	
	public ParentResponse createParent(ParentRequest request) {
		Parent parent = parentMapper.toEntity(request);
		
		if (request.getStudentIds() != null && !request.getStudentIds().isEmpty()) {
			final List<Student> students = studentRepository.findAllById(request.getStudentIds());
			// assign parent to each student
			for (Student student : students) {
				student.setParent(parent);
			}
		}
		
		parent = parentRepository.save(parent);
		return parentMapper.toResponse(parent);
	}
	
	public ParentResponse updateParent(Long parentId, ParentRequest request) {
		return null;
	}
	
	public void deleteParent(Long parentId) {
	
	}
	
	public ParentResponse getParent(Long id) {
		return parentRepository.findById(id)
			        .map(parentMapper::toResponse)
			        .orElseThrow(() -> new RuntimeException("Parent not found"));
	}
	
	public ParentResponse getParentByPhone(String phone) {
		return null;
	}
	
	public ParentResponse getParentByEmail(String email) {
		return null;
	}
	
	public ParentResponse getParentWithIdentity(Long parentId) {
		return null;
	}
	
	public PageResponse<ParentResponse> getAllParents(Pageable pageable) {
		Page<Parent> page = parentRepository.findAll(pageable);
		return PageResponse.<ParentResponse>builder()
			        .data(page.getContent().stream().map(parentMapper::toResponse).toList())
			        .page(page.getNumber())
			        .size(page.getSize())
			        .totalElements(page.getTotalElements())
			        .totalPages(page.getTotalPages())
			        .hasNext(page.hasNext())
			        .hasPrevious(page.hasPrevious())
			        .build();
	}
	
	public PageResponse<ParentResponse> getParentsBySchool(Long schoolId, Pageable pageable) {
		return null;
	}
	
	public List<ParentResponse> getParentsWithMultipleChildren(Long schoolId) {
		return List.of();
	}
	
	public PageResponse<ParentResponse> searchParentsByName(String name, Pageable pageable) {
		return null;
	}
	
}