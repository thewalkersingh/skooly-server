package com.skooly.service.impl;

import com.skooly.dto.request.SubjectRequest;
import com.skooly.dto.response.SubjectResponse;
import com.skooly.entity.Subject;
import com.skooly.entity.Teacher;
import com.skooly.enums.SubjectStatus;
import com.skooly.exception.ResourceNotFoundException;
import com.skooly.mapper.SubjectMapper;
import com.skooly.repository.SubjectRepository;
import com.skooly.repository.TeacherRepository;
import com.skooly.service.SubjectService;
import com.skooly.wrapper.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {
	
	private final SubjectRepository subjectRepository;
	private final SubjectMapper subjectMapper;
	private final TeacherRepository teacherRepository;
	
	// ── Create / Update / Delete ──────────────────────────────────────────────
	public SubjectResponse createSubject(SubjectRequest request) {
		Subject subject = subjectMapper.toEntity(request);
		subject = subjectRepository.save(subject);
		return subjectMapper.toResponse(subject);
	}
	
	public SubjectResponse updateSubject(Long subjectId, SubjectRequest request) {
		subjectRepository.findById(subjectId)
			.orElseThrow(() -> new RuntimeException("Subject not found"));
		Subject subject = subjectMapper.toEntity(request);
		Subject response = subjectRepository.save(subject);
		return subjectMapper.toResponse(response);
	}
	
	public void deleteSubject(Long subjectId) {
		subjectRepository.findById(subjectId)
			.orElseThrow(() -> new ResourceNotFoundException("Subject not found"));
		subjectRepository.deleteById(subjectId);
	}
	
	// ── Single fetch ──────────────────────────────────────────────────────────
	public SubjectResponse getSubject(Long id) {
		return subjectRepository.findById(id)
			       .map(subjectMapper::toResponse)
			       .orElseThrow(() -> new RuntimeException("Subject not found"));
	}
	
	public SubjectResponse getSubjectByCode(String subjectCode) {
		Subject response = subjectRepository.findBySubjectCode(subjectCode)
			                   .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));
		return subjectMapper.toResponse(response);
		
	}
	
	public SubjectResponse getSubjectWithTeachers(Long subjectId) {
		Subject subject = subjectRepository.findSubjectsByIdWithTeachers(subjectId)
			                  .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));
		return subjectMapper.toResponse(subject);
	}
	
	// ── Lists ─────────────────────────────────────────────────────────────────
	public PageResponse<SubjectResponse> getAllSubjects(Pageable pageable) {
		Page<Subject> page = subjectRepository.findAll(pageable);
		List<SubjectResponse> list = page.getContent().stream().map(subjectMapper::toResponse).toList();
		return PageResponse.<SubjectResponse>builder()
			       .data(list)
			       .page(page.getNumber())
			       .size(page.getSize())
			       .totalElements(page.getTotalElements())
			       .totalPages(page.getTotalPages())
			       .hasNext(page.hasNext())
			       .hasPrevious(page.hasPrevious())
			       .build();
	}
	
	public PageResponse<SubjectResponse> getSubjectsByStatus(SubjectStatus status, Pageable pageable) {
		Page<Subject> page = subjectRepository.findByStatus(status, pageable);
		List<SubjectResponse> list = page.getContent().stream().map(subjectMapper::toResponse).toList();
		return PageResponse.<SubjectResponse>builder()
			       .data(list)
			       .page(page.getNumber())
			       .size(page.getSize())
			       .totalElements(page.getTotalElements())
			       .totalPages(page.getTotalPages())
			       .hasNext(page.hasNext())
			       .hasPrevious(page.hasPrevious())
			       .build();
		
	}
	
	public List<SubjectResponse> getSubjectsBySection(Long sectionId) {
		List<Subject> list = subjectRepository.findBySectionId(sectionId);
		return list.stream().map(subjectMapper::toResponse).toList();
	}
	
	public List<SubjectResponse> getSubjectsByTeacher(Long teacherId) {
		List<Subject> list = subjectRepository.findByTeacherId(teacherId);
		return list.stream().map(subjectMapper::toResponse).toList();
	}
	
	// ── Search ────────────────────────────────────────────────────────────────
	public PageResponse<SubjectResponse> searchSubjectsByName(String name, Pageable pageable) {
		Page<Subject> page = subjectRepository.findBySubjectNameContainingIgnoreCase(name, pageable);
		List<SubjectResponse> list = page.getContent().stream().map(subjectMapper::toResponse).toList();
		return PageResponse.<SubjectResponse>builder()
			       .data(list)
			       .page(page.getNumber())
			       .size(page.getSize())
			       .totalElements(page.getTotalElements())
			       .totalPages(page.getTotalPages())
			       .hasNext(page.hasNext())
			       .hasPrevious(page.hasPrevious())
			       .build();
	}
	
	// ── Status management ─────────────────────────────────────────────────────
	public SubjectResponse updateStatus(Long subjectId, SubjectStatus status) {
		Subject subject = subjectRepository.findById(subjectId)
			                  .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));
		subject.setStatus(status);
		subjectRepository.save(subject);
		return subjectMapper.toResponse(subject);
	}
	
	// ── Teacher assignment ────────────────────────────────────────────────────
	public SubjectResponse assignTeacher(Long subjectId, Long teacherId) {
		Subject subject = subjectRepository.findSubjectsByIdWithTeachers(subjectId)
			                  .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));
		Teacher teacher = teacherRepository.findById(teacherId)
			                  .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
		// Guard — avoid duplicate entry in subject_teachers join table
		boolean alreadyAssigned = subject.getTeachers()
			                          .stream().anyMatch(t -> t.getId().equals(teacherId));
		if (alreadyAssigned) {
			throw new IllegalStateException("Teacher already assigned to this subject");
		}
		subject.getTeachers().add(teacher);
		Subject saved = subjectRepository.save(subject);
		return subjectMapper.toResponse(saved);
	}
	
	public SubjectResponse removeTeacher(Long subjectId, Long teacherId) {
		Subject subject = subjectRepository.findSubjectsByIdWithTeachers(subjectId)
			                  .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));
		
		boolean exists = subject.getTeachers()
			                 .stream()
			                 .anyMatch(t -> t.getId().equals(teacherId));
		
		if (!exists) {
			throw new ResourceNotFoundException("Teacher not assigned to this subject");
		}
		
		subject.getTeachers().removeIf(t -> t.getId().equals(teacherId));
		Subject saved = subjectRepository.save(subject);
		return subjectMapper.toResponse(saved);
	}
	
	// ── Assignment utilities ──────────────────────────────────────────────────
	public List<SubjectResponse> getSubjectsNotInSection(Long sectionId) {
		List<Subject> list = subjectRepository.findSubjectsNotInSection(sectionId);
		return list.stream().map(subjectMapper::toResponse).toList();
	}
	
	public List<SubjectResponse> getSubjectsNotAssignedToTeacher(Long teacherId) {
		List<Subject> list = subjectRepository.findSubjectsNotAssignedToTeacher(teacherId);
		return list.stream().map(subjectMapper::toResponse).toList();
	}
	
	private boolean existsBySubjectCode(String subjectCode) {
		return subjectRepository.existsBySubjectCode(subjectCode);
	}
	
	private List<Subject> findByStatus(SubjectStatus status) {
		return subjectRepository.findByStatus(status);
	}
	
}