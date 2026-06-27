package com.skooly.service.impl;

import com.skooly.dto.request.TeacherRequest;
import com.skooly.dto.response.SectionResponse;
import com.skooly.dto.response.TeacherResponse;
import com.skooly.entity.School;
import com.skooly.entity.Teacher;
import com.skooly.enums.TeacherStatus;
import com.skooly.exception.ResourceNotFoundException;
import com.skooly.mapper.SectionMapper;
import com.skooly.mapper.TeacherMapper;
import com.skooly.repository.SchoolRepository;
import com.skooly.repository.SectionRepository;
import com.skooly.repository.TeacherRepository;
import com.skooly.service.TeacherService;
import com.skooly.wrapper.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
	
	private final TeacherRepository teacherRepository;
	private final TeacherMapper teacherMapper;
	private final SchoolRepository schoolRepository;
	private final SectionRepository sectionRepository;
	private final SectionMapper sectionMapper;
	
	// ── Create / Update / Delete ──────────────────────────────────────────────
	public TeacherResponse createTeacher(Long schoolId, TeacherRequest request) {
		// Validate no duplicate phone/email
		if (teacherRepository.existsByIdentityPhone(request.getIdentity()
		                                                   .getPhone())) {
			throw new IllegalStateException("Phone already registered");
		}
		if (request.getIdentity()
		           .getEmail() != null && teacherRepository.existsByIdentityEmail(
			request.getIdentity()
			       .getEmail())) {
			throw new IllegalStateException("Email already registered");
		}
		Teacher teacher = teacherMapper.toEntity(request);
		School school = schoolRepository
			                .findById(schoolId)
			                .orElseThrow(() -> new RuntimeException("School not found"));
		teacher.setSchool(school);
		teacher = teacherRepository.save(teacher);
		return teacherMapper.toResponse(teacher);
	}
	
	public TeacherResponse updateTeacher(Long teacherId, TeacherRequest request) {
		teacherRepository.findById(teacherId)
		                 .orElseThrow(() -> new RuntimeException("Teacher not found"));
		Teacher teacher = teacherMapper.toEntity(request);
		Teacher response = teacherRepository.save(teacher);
		return teacherMapper.toResponse(response);
	}
	
	public void deleteTeacher(Long teacherId) {
		Teacher teacher = teacherRepository
			                  .findById(teacherId)
			                  .orElseThrow(() -> new RuntimeException("Teacher not found"));
		teacher.setStatus(TeacherStatus.DELETED);
		teacherRepository.save(teacher);
	}
	
	// ── Single fetch ──────────────────────────────────────────────────────────
	public TeacherResponse getTeacher(Long id) {
		
		return teacherRepository
			       .findById(id)
			       .map(teacherMapper::toResponse)
			       .orElseThrow(() -> new RuntimeException("Teacher not found"));
	}
	
	public TeacherResponse getTeacherByPhone(String phone) {
		
		return teacherRepository
			       .findByIdentityPhone(phone)
			       .map(teacherMapper::toResponse)
			       .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with phone: " + phone));
		
	}
	
	public TeacherResponse getTeacherByEmail(String email) {
		
		return teacherRepository
			       .findByIdentityEmail(email)
			       .map(teacherMapper::toResponse)
			       .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with email: " + email));
	}
	
	public TeacherResponse getClassTeacherBySection(Long sectionId) {
		Teacher teacher = teacherRepository
			                  .findClassTeacherBySectionId(sectionId)
			                  .orElseThrow(() -> new RuntimeException("Teacher not found"));
		return teacherMapper.toResponse(teacher);
	}
	
	// ── Lists ─────────────────────────────────────────────────────────────────
	public PageResponse<TeacherResponse> getAllTeachers(Pageable pageable) {
		Page<Teacher> page = teacherRepository.findAll(pageable);
		List<TeacherResponse> response = page.getContent()
		                                     .stream()
		                                     .map(teacherMapper::toResponse)
		                                     .toList();
		return PageResponse
			       .<TeacherResponse>builder()
			       .data(response)
			       .page(page.getNumber())
			       .size(page.getSize())
			       .totalElements(page.getTotalElements())
			       .totalPages(page.getTotalPages())
			       .hasNext(page.hasNext())
			       .hasPrevious(page.hasPrevious())
			       .build();
	}
	
	public PageResponse<TeacherResponse> getTeachersBySchool(Long schoolId, Pageable pageable) {
		Page<Teacher> page = teacherRepository.findBySchoolId(schoolId, pageable);
		List<TeacherResponse> responses = page.getContent()
		                                      .stream()
		                                      .map(teacherMapper::toResponse)
		                                      .toList();
		return PageResponse
			       .<TeacherResponse>builder()
			       .data(responses)
			       .page(page.getNumber())
			       .size(page.getSize())
			       .totalElements(page.getTotalElements())
			       .totalPages(page.getTotalPages())
			       .hasNext(page.hasNext())
			       .hasPrevious(page.hasPrevious())
			       .build();
	}
	
	public PageResponse<TeacherResponse> getTeachersBySchoolAndStatus(Long schoolId, TeacherStatus status,
		Pageable pageable) {
		Page<Teacher> page = teacherRepository.findBySchoolIdAndStatus(schoolId, status, pageable);
		List<TeacherResponse> responses = page.getContent()
		                                      .stream()
		                                      .map(teacherMapper::toResponse)
		                                      .toList();
		return PageResponse
			       .<TeacherResponse>builder()
			       .data(responses)
			       .page(page.getNumber())
			       .size(page.getSize())
			       .totalElements(page.getTotalElements())
			       .totalPages(page.getTotalPages())
			       .hasNext(page.hasNext())
			       .hasPrevious(page.hasPrevious())
			       .build();
	}
	
	public List<TeacherResponse> getTeachersBySubject(Long subjectId) {
		List<Teacher> response = teacherRepository.findTeachersBySubjectId(subjectId);
		return response.stream()
		               .map(teacherMapper::toResponse)
		               .toList();
	}
	
	// ── Search ────────────────────────────────────────────────────────────────
	public PageResponse<TeacherResponse> searchTeachersByName(Long schoolId, String name, Pageable pageable) {
		Page<Teacher> page = teacherRepository.searchByNameAndSchoolId(schoolId, name, pageable);
		List<TeacherResponse> responses = page.getContent()
		                                      .stream()
		                                      .map(teacherMapper::toResponse)
		                                      .toList();
		return PageResponse
			       .<TeacherResponse>builder()
			       .data(responses)
			       .page(page.getNumber())
			       .size(page.getSize())
			       .totalElements(page.getTotalElements())
			       .totalPages(page.getTotalPages())
			       .hasNext(page.hasNext())
			       .hasPrevious(page.hasPrevious())
			       .build();
	}
	
	// ── Status management ─────────────────────────────────────────────────────
	public TeacherResponse updateStatus(Long teacherId, TeacherStatus status) {
		Teacher teacher =
			teacherRepository.findById(teacherId)
			                 .orElseThrow(() -> new RuntimeException("Teacher not found"));
		teacher.setStatus(status);
		teacherRepository.save(teacher);
		return teacherMapper.toResponse(teacher);
	}
	
	// ── Unassigned teachers (admin utility) ───────────────────────────────────
	public List<TeacherResponse> getUnassignedTeachers(Long schoolId) {
		List<Teacher> teachers = teacherRepository.findUnassignedTeachersBySchoolId(schoolId);
		return teachers.stream()
		               .map(teacherMapper::toResponse)
		               .toList();
	}
	
	public List<SectionResponse> getSectionsByTeacher(Long teacherId) {
		if (!teacherRepository.existsById(teacherId)) {
			throw new ResourceNotFoundException("Teacher not found");
		}
		return sectionRepository.findByTeacherId(teacherId)
		                        .stream()
		                        .map(sectionMapper::toResponse)
		                        .toList();
	}
	
}