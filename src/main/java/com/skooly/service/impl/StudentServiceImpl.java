package com.skooly.service.impl;
import com.skooly.dto.common.PageResponse;
import com.skooly.dto.request.CreateStudentRequest;
import com.skooly.dto.request.UpdateStudentRequest;
import com.skooly.dto.response.StudentResponse;
import com.skooly.dto.response.StudentSummaryResponse;
import com.skooly.exception.BadRequestException;
import com.skooly.exception.ResourceNotFoundException;
import com.skooly.mapper.StudentMapper;
import com.skooly.model.*;
import com.skooly.repository.*;
import com.skooly.service.StudentService;
import com.skooly.utils.FileUploadUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentServiceImpl implements StudentService {
	private final StudentRepository studentRepository;
	private final SchoolClassRepository classRepository;
	private final SectionRepository sectionRepository;
	private final ParentRepository parentRepository;
	private final StudentMapper studentMapper;
	private final FileUploadUtils fileUploadUtils;
	
	@Override
	@Transactional(readOnly = true)
	public PageResponse<StudentSummaryResponse> getAllStudents(
			int page, int size, String search,
			Long classId, Long sectionId,
			Student.Status status, Student.Gender gender) {
		
		Pageable pageable = PageRequest.of(page-1, size, Sort.by("firstName").ascending());
		
		Page<Student> students = studentRepository.findWithFilters(
				classId, sectionId, status, gender, search, pageable
		                                                          );
		
		List<StudentSummaryResponse> data = students.getContent()
				                                    .stream()
				                                    .map(studentMapper::toSummaryResponse)
				                                    .toList();
		
		return new PageResponse<>(data, page, size, students.getTotalElements(), students.getTotalPages());
	}
	
	@Override
	@Transactional(readOnly = true)
	public StudentResponse getStudentById(Long id) {
		return studentMapper.toResponse(findStudentById(id));
	}
	
	@Override
	@Transactional(readOnly = true)
	public StudentResponse getMyProfile(Long userId) {
		Student student = studentRepository
				                  .findByUserId(userId)
				                  .orElseThrow(() -> new ResourceNotFoundException("Student profile not found"));
		return studentMapper.toResponse(student);
	}
	
	@Override
	public StudentResponse createStudent(CreateStudentRequest request) {
		if(request.getEmail() != null && studentRepository.existsByEmail(request.getEmail())){
			throw new BadRequestException("Email already in use");
		}
		
		Student student = studentMapper.toEntity(request);
		
		student.setSchoolClass(findClassById(request.getClassId()));
		student.setSection(findSectionById(request.getSectionId()));
		
		if(request.getParentId() != null){
			student.setParent(findParentById(request.getParentId()));
		}
		
		student.setStatus(Student.Status.ACTIVE);
		
		return studentMapper.toResponse(studentRepository.save(student));
	}
	
	@Override
	public StudentResponse updateStudent(Long id, UpdateStudentRequest request) {
		Student student = findStudentById(id);
		
		if(request.getFirstName() != null)
			student.setFirstName(request.getFirstName());
		if(request.getLastName() != null)
			student.setLastName(request.getLastName());
		if(request.getDob() != null)
			student.setDob(request.getDob());
		if(request.getGender() != null)
			student.setGender(request.getGender());
		if(request.getAddress() != null)
			student.setAddress(request.getAddress());
		if(request.getPhone() != null)
			student.setPhone(request.getPhone());
		if(request.getEmail() != null)
			student.setEmail(request.getEmail());
		if(request.getClassId() != null)
			student.setSchoolClass(findClassById(request.getClassId()));
		if(request.getSectionId() != null)
			student.setSection(findSectionById(request.getSectionId()));
		if(request.getParentId() != null)
			student.setParent(findParentById(request.getParentId()));
		
		return studentMapper.toResponse(studentRepository.save(student));
	}
	
	@Override
	public void deleteStudent(Long id) throws ResourceNotFoundException {
		if(!studentRepository.existsById(id)){
			throw new ResourceNotFoundException("Student not found with id: "+id);
		}
		studentRepository.deleteById(id);
	}
	
	@Override
	public void updateStatus(Long id, Student.Status status) {
		Student student = findStudentById(id);
		student.setStatus(status);
		studentRepository.save(student);
	}
	
	@Override
	public StudentResponse uploadPhoto(Long id, MultipartFile file) {
		Student student = findStudentById(id);
		String photoPath = fileUploadUtils.uploadFile(file, "students");
		if(student.getPhoto() != null){
			fileUploadUtils.deleteFile(student.getPhoto());
		}
		student.setPhoto(photoPath);
		return studentMapper.toResponse(studentRepository.save(student));
	}
	
	@Override
	public void deletePhoto(Long id) {
		Student student = findStudentById(id);
		if(student.getPhoto() != null){
			fileUploadUtils.deleteFile(student.getPhoto());
			student.setPhoto(null);
			studentRepository.save(student);
		}
	}
	
	// ── Private helpers ──────────────────────────────────────────────────────
	
	private Student findStudentById(Long id) throws ResourceNotFoundException {
		return studentRepository.findById(id)
				       .orElseThrow(() -> new ResourceNotFoundException(
						       "Student not found with id: "+id));
	}
	
	private SchoolClass findClassById(Long id) {
		return classRepository.findById(id)
				       .orElseThrow(() -> new ResourceNotFoundException(
						       "Class not found with id: "+id));
	}
	
	private Section findSectionById(Long id) {
		return sectionRepository.findById(id)
				       .orElseThrow(() -> new ResourceNotFoundException(
						       "Section not found with id: "+id));
	}
	
	private Parent findParentById(Long id) {
		return parentRepository.findById(id)
				       .orElseThrow(() -> new ResourceNotFoundException(
						       "Parent not found with id: "+id));
	}
}