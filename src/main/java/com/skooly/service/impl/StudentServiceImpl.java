package com.skooly.service.impl;

import com.skooly.dto.request.StudentRequest;
import com.skooly.dto.response.SectionResponse;
import com.skooly.dto.response.StudentResponse;
import com.skooly.entity.Parent;
import com.skooly.entity.Section;
import com.skooly.entity.Student;
import com.skooly.entity.Subject;
import com.skooly.enums.StudentStatus;
import com.skooly.mapper.StudentMapper;
import com.skooly.repository.ParentRepository;
import com.skooly.repository.SectionRepository;
import com.skooly.repository.StudentRepository;
import com.skooly.service.StudentService;
import com.skooly.wrapper.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
	
	private final StudentRepository studentRepository;
	private final StudentMapper studentMapper;
	private final SectionRepository sectionRepository;
	private final ParentRepository parentRepository;
	
	public StudentResponse createStudent(StudentRequest request) {
		Student student = studentMapper.toEntity(request);
		
		// ✅ attach Section
		Section section = sectionRepository.findById(request.getSectionId())
			                  .orElseThrow(() -> new RuntimeException("Section not found"));
		student.setSection(section);
		
		// ✅ attach Parent (optional)
		if (request.getParentId() != null) {
			Parent parent = parentRepository.findById(request.getParentId())
				                .orElseThrow(() -> new RuntimeException("Parent not found"));
			student.setParent(parent);
		}
		
		student = studentRepository.save(student);
		
		// ✅ build response
		StudentResponse response = studentMapper.toResponse(student);
		response.setSubjectIds(
			section.getSubjects().stream().map(Subject::getId).toList()
		);
		return response;
	}
	
	public StudentResponse createStudent(Long sectionId, StudentRequest request) {
		return null;
	}
	
	public StudentResponse updateStudent(Long studentId, StudentRequest request) {
		return null;
	}
	
	public void deleteStudent(Long studentId) {
	
	}
	
	public StudentResponse getStudent(Long id) {
		return studentRepository.findById(id)
			       .map(studentMapper::toResponse)
			       .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
	}
	
	public StudentResponse getStudentWithDetails(Long studentId) {
		return null;
	}
	
	public StudentResponse getStudentByPhone(String phone) {
		return null;
	}
	
	public StudentResponse getStudentByEmail(String email) {
		return null;
	}
	
	public SectionResponse getSectionByStudent(Long studentId) {
		return null;
	}
	
	public PageResponse<StudentResponse> getAllStudents(Pageable pageable) {
		Page<Student> page = studentRepository.findAll(pageable);
		return PageResponse.<StudentResponse>builder()
			       .data(page.getContent().stream().map(studentMapper::toResponse).toList())
			       .page(page.getNumber())
			       .size(page.getSize())
			       .totalElements(page.getTotalElements())
			       .totalPages(page.getTotalPages())
			       .hasNext(page.hasNext())
			       .hasPrevious(page.hasPrevious())
			       .build();
	}
	
	public PageResponse<StudentResponse> getStudentsBySection(Long sectionId, Pageable pageable) {
		return null;
	}
	
	public PageResponse<StudentResponse> getStudentsBySectionAndStatus(Long sectionId, StudentStatus status,
		Pageable pageable) {
		return null;
	}
	
	public PageResponse<StudentResponse> getStudentsByClassroom(Long classroomId, Pageable pageable) {
		return null;
	}
	
	public PageResponse<StudentResponse> getStudentsBySchool(Long schoolId, Pageable pageable) {
		return null;
	}
	
	public List<StudentResponse> getStudentsByParent(Long parentId) {
		return List.of();
	}
	
	public PageResponse<StudentResponse> searchStudentsByName(Long schoolId, String name, Pageable pageable) {
		return null;
	}
	
	public StudentResponse updateStatus(Long studentId, StudentStatus status) {
		return null;
	}
	
	public StudentResponse transferSection(Long studentId, Long newSectionId) {
		return null;
	}
	
	public StudentResponse assignParent(Long studentId, Long parentId) {
		return null;
	}
	
	public StudentResponse removeParent(Long studentId) {
		return null;
	}
	
	public long countStudentsBySection(Long sectionId) {
		return 0;
	}
	
	public long countStudentsBySchool(Long schoolId) {
		return 0;
	}
	
	public List<StudentResponse> getStudentsWithoutParent(Long schoolId) {
		return List.of();
	}
	
}