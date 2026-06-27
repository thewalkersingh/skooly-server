package com.skooly.service.impl;

import com.skooly.dto.request.StudentRequest;
import com.skooly.dto.response.SectionResponse;
import com.skooly.dto.response.StudentResponse;
import com.skooly.entity.Parent;
import com.skooly.entity.Section;
import com.skooly.entity.Student;
import com.skooly.enums.StudentStatus;
import com.skooly.exception.ResourceNotFoundException;
import com.skooly.mapper.SectionMapper;
import com.skooly.mapper.StudentMapper;
import com.skooly.repository.ParentRepository;
import com.skooly.repository.SectionRepository;
import com.skooly.repository.StudentRepository;
import com.skooly.service.StudentService;
import com.skooly.wrapper.PageResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentServiceImpl implements StudentService {
	
	private final StudentRepository studentRepository;
	private final StudentMapper studentMapper;
	private final SectionRepository sectionRepository;
	private final SectionMapper sectionMapper;
	private final ParentRepository parentRepository;
	
	// ── Create / Update / Delete ──────────────────────────────────────────────
	public StudentResponse createStudent(Long sectionId, StudentRequest request) {
		if (studentRepository.existsByIdentityPhone(request.getIdentity()
			                                            .getPhone())) {
			throw new IllegalStateException("Phone already registered");
		}
		if (request.getIdentity()
			    .getEmail() != null && studentRepository.existsByIdentityEmail(
			request.getIdentity()
				.getEmail())) {
			throw new IllegalStateException("Email already registered");
		}
		Student student = studentMapper.toEntity(request);
		Section section =
			sectionRepository.findByIdWithClassroom(sectionId)
				.orElseThrow(() -> new ResourceNotFoundException("Section not found"));
		student.setSection(section);
		student = studentRepository.save(student);
		
		return studentMapper.toResponse(student);
	}
	
	public StudentResponse updateStudent(Long studentId, StudentRequest request) {
		studentRepository
			.findById(studentId)
			.orElseThrow(() -> new RuntimeException("Student not found"));
		Student student = studentMapper.toEntity(request);
		student = studentRepository.save(student);
		return studentMapper.toResponse(student);
	}
	
	public void deleteStudent(Long studentId) {
		Student student =
			studentRepository
				.findById(studentId)
				.orElseThrow(() -> new ResourceNotFoundException("No student found"));
		student.setStudentStatus(StudentStatus.DELETED);
		studentRepository.save(student);
	}
	
	// ── Single fetch ──────────────────────────────────────────────────────────
	@GetMapping("/{studentId}")
	public StudentResponse getStudent(Long studentId) {
		return studentMapper.toResponse(
			studentRepository
				.findById(studentId)
				.orElseThrow(() -> new ResourceNotFoundException("No student found")));
	}
	
	public StudentResponse getStudentWithDetails(Long id) {
		return studentRepository
			       .findByIdWithDetails(id)
			       .map(studentMapper::toResponse)
			       .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
	}
	
	public StudentResponse getStudentByPhone(String phone) {
		if (studentRepository.existsByIdentityPhone(phone)) {
			throw new IllegalStateException("Phone already registered");
		}
		Student student =
			studentRepository.findByIdentityPhone(phone)
				.orElseThrow(
					() -> new RuntimeException("Student not found with id: " + phone));
		return studentMapper.toResponse(student);
	}
	
	public StudentResponse getStudentByEmail(String email) {
		if (studentRepository.existsByIdentityEmail(email)) {
			throw new IllegalStateException("Email already registered");
		}
		Student student =
			studentRepository.findByIdentityEmail(email)
				.orElseThrow(
					() -> new RuntimeException("Student not found with id: " + email));
		return studentMapper.toResponse(student);
	}
	
	public SectionResponse getSectionByStudent(Long studentId) {
		Student student =
			studentRepository.findById(studentId)
				.orElseThrow(
					() -> new RuntimeException("Student not found with id: " + studentId));
		return sectionMapper.toResponse(student.getSection());
	}
	
	// ── Lists ─────────────────────────────────────────────────────────────────
	public PageResponse<StudentResponse> getAllStudents(Pageable pageable) {
		Page<Student> page = studentRepository.findAll(pageable);
		List<StudentResponse> responses = page.getContent()
			                                  .stream()
			                                  .map(studentMapper::toResponse)
			                                  .toList();
		return PageResponse.<StudentResponse>builder()
			       .data(responses)
			       .page(page.getNumber())
			       .size(page.getSize())
			       .totalElements(page.getTotalElements())
			       .totalPages(page.getTotalPages())
			       .hasNext(page.hasNext())
			       .hasPrevious(page.hasPrevious())
			       .build();
	}
	
	public PageResponse<StudentResponse> getStudentsBySection(Long sectionId, Pageable pageable) {
		Page<Student> page = studentRepository.findBySectionId(sectionId, pageable);
		List<StudentResponse> responses = page.getContent()
			                                  .stream()
			                                  .map(studentMapper::toResponse)
			                                  .toList();
		return PageResponse.<StudentResponse>builder()
			       .data(responses)
			       .page(page.getNumber())
			       .size(page.getSize())
			       .totalElements(page.getTotalElements())
			       .totalPages(page.getTotalPages())
			       .hasNext(page.hasNext())
			       .hasPrevious(page.hasPrevious())
			       .build();
	}
	
	public PageResponse<StudentResponse> getStudentsBySectionAndStatus(Long sectionId, StudentStatus status,
		Pageable pageable) {
		Page<Student> page = studentRepository.findBySectionIdAndStudentStatus(sectionId, status, pageable);
		List<StudentResponse> responses = page.getContent()
			                                  .stream()
			                                  .map(studentMapper::toResponse)
			                                  .toList();
		return PageResponse.<StudentResponse>builder()
			       .data(responses)
			       .page(page.getNumber())
			       .size(page.getSize())
			       .totalElements(page.getTotalElements())
			       .totalPages(page.getTotalPages())
			       .hasNext(page.hasNext())
			       .hasPrevious(page.hasPrevious())
			       .build();
	}
	
	public PageResponse<StudentResponse> getStudentsByClassroom(Long classroomId, Pageable pageable) {
		Page<Student> page = studentRepository.findByClassroomId(classroomId, pageable);
		List<StudentResponse> responses = page.getContent()
			                                  .stream()
			                                  .map(studentMapper::toResponse)
			                                  .toList();
		return PageResponse.<StudentResponse>builder()
			       .data(responses)
			       .page(page.getNumber())
			       .size(page.getSize())
			       .totalElements(page.getTotalElements())
			       .totalPages(page.getTotalPages())
			       .hasNext(page.hasNext())
			       .hasPrevious(page.hasPrevious())
			       .build();
	}
	
	public PageResponse<StudentResponse> getStudentsBySchool(Long schoolId, Pageable pageable) {
		Page<Student> page = studentRepository.findBySchoolId(schoolId, pageable);
		List<StudentResponse> responses = page.getContent()
			                                  .stream()
			                                  .map(studentMapper::toResponse)
			                                  .toList();
		return PageResponse.<StudentResponse>builder()
			       .data(responses)
			       .page(page.getNumber())
			       .size(page.getSize())
			       .totalElements(page.getTotalElements())
			       .totalPages(page.getTotalPages())
			       .hasNext(page.hasNext())
			       .hasPrevious(page.hasPrevious())
			       .build();
	}
	
	// Students linked to a parent
	public List<StudentResponse> getStudentsByParent(Long parentId) {
		List<Student> students = studentRepository.findByParentId(parentId);
		return students.stream()
			       .map(studentMapper::toResponse)
			       .toList();
	}
	
	// ── Search ────────────────────────────────────────────────────────────────
	public PageResponse<StudentResponse> searchStudentsByName(Long schoolId, String name, Pageable pageable) {
		Page<Student> page = studentRepository.searchByNameAndSchoolId(schoolId, name, pageable);
		List<StudentResponse> responses = page.getContent()
			                                  .stream()
			                                  .map(studentMapper::toResponse)
			                                  .toList();
		return PageResponse.<StudentResponse>builder()
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
	public StudentResponse updateStatus(Long studentId, StudentStatus status) {
		Student student =
			studentRepository.findById(studentId)
				.orElseThrow(() -> new RuntimeException("Student not found"));
		student.setStudentStatus(status);
		studentRepository.save(student);
		return studentMapper.toResponse(student);
	}
	
	// ── Section transfer ──────────────────────────────────────────────────────
	// Moves a student from their current section to a new one
	public StudentResponse transferSection(Long studentId, Long newSectionId) {
		Student student =
			studentRepository.findByIdWithDetails(studentId)
				.orElseThrow(() -> new RuntimeException("Student not found"));
		Section section =
			sectionRepository.findById(newSectionId)
				.orElseThrow(() -> new RuntimeException("Section not found"));
		student.setSection(section);
		studentRepository.save(student);
		return studentMapper.toResponse(student);
	}
	
	// ── Parent assignment ─────────────────────────────────────────────────────
	public StudentResponse assignParent(Long studentId, Long parentId) {
		Student student =
			studentRepository.findByIdWithDetails(studentId)
				.orElseThrow(() -> new RuntimeException("Student not found"));
		Parent parent = parentRepository.findById(parentId)
			                .orElseThrow(() -> new RuntimeException("Parent not found"));
		student.setParent(parent);
		studentRepository.save(student);
		return studentMapper.toResponse(student);
	}
	
	public StudentResponse removeParent(Long studentId) {
		Student student =
			studentRepository.findByIdWithDetails(studentId)
				.orElseThrow(() -> new RuntimeException("Student not found"));
		student.setParent(null);
		studentRepository.save(student);
		return studentMapper.toResponse(student);
	}
	
	// ── Stats ─────────────────────────────────────────────────────────────────
	public long countStudentsBySection(Long sectionId) {
		return studentRepository.countBySectionId(sectionId);
	}
	
	public long countStudentsBySchool(Long schoolId) {
		return studentRepository.countBySchoolId(schoolId);
	}
	
	// ── Admin utilities ───────────────────────────────────────────────────────
	public List<StudentResponse> getStudentsWithoutParent(Long schoolId) {
		List<Student> students = studentRepository.findStudentsWithoutParentBySchoolId(schoolId);
		return students.stream()
			       .map(studentMapper::toResponse)
			       .toList();
	}
	
}