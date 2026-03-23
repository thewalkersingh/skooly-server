package com.skooly.service.impl;
import com.skooly.constant.Gender;
import com.skooly.constant.Status;
import com.skooly.dto.request.StudentRequest;
import com.skooly.dto.response.StudentResponse;
import com.skooly.exception.BadRequestException;
import com.skooly.exception.ResourceNotFoundException;
import com.skooly.model.*;
import com.skooly.repository.*;
import com.skooly.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
	private final StudentRepository studentRepository;
	private final UserRepository userRepository;
	private final SchoolRepository schoolRepository;
	private final SchoolClassRepository classRepository;
	private final SectionRepository sectionRepository;
	private final ParentRepository parentRepository;
	
	public List<StudentResponse> getAllStudents(Long schoolId) {
		return studentRepository.findBySchoolId(schoolId)
				       .stream().map(StudentResponse::from).toList();
	}
	
	public StudentResponse getStudentById(Long schoolId, Long studentId) {
		Student student = studentRepository.findByIdAndSchoolId(studentId, schoolId)
				                  .orElseThrow(() -> new ResourceNotFoundException("Student", studentId));
		return StudentResponse.from(student);
	}
	
	public List<StudentResponse> searchStudents(Long schoolId, String query) {
		return studentRepository.searchBySchoolId(schoolId, query)
				       .stream().map(StudentResponse::from).toList();
	}
	
	public long countStudents(Long schoolId) {
		return studentRepository.countBySchoolId(schoolId);
	}
	
	@Transactional
	public StudentResponse createStudent(Long schoolId, StudentRequest request) {
		School school = schoolRepository.findById(schoolId)
				                .orElseThrow(() -> new ResourceNotFoundException("School", schoolId));
		
		// Check username uniqueness
		if(userRepository.existsBySchoolIdAndUsername(schoolId, request.getUsername())){
			throw new BadRequestException("Username '"+request.getUsername()+"' already exists");
		}
		
		// Create user account atomically
		User user = User.builder()
				            .school(school)
				            .username(request.getUsername())
				            .password(request.getPassword()) // TODO: BCrypt encode when JWT added
				            .role("STUDENT")
				            .isActive(true)
				            .build();
		user = userRepository.save(user);
		
		// Resolve optional relations
		SchoolClass schoolClass = null;
		if(request.getClassId() != null){
			schoolClass = classRepository.findById(request.getClassId())
					              .orElseThrow(() -> new ResourceNotFoundException("Class", request.getClassId()));
		}
		
		Section section = null;
		if(request.getSectionId() != null){
			section = sectionRepository.findById(request.getSectionId())
					          .orElseThrow(() -> new ResourceNotFoundException("Section", request.getSectionId()));
		}
		
		Parent parent = null;
		if(request.getParentId() != null){
			parent = parentRepository.findById(request.getParentId())
					         .orElseThrow(() -> new ResourceNotFoundException("Parent", request.getParentId()));
		}
		
		Student student = Student.builder()
				                  .school(school)
				                  .user(user)
				                  .firstName(request.getFirstName())
				                  .lastName(request.getLastName())
				                  .dob(request.getDob())
				                  .gender(request.getGender() != null ? Gender.valueOf(request.getGender()) : null)
				                  .address(request.getAddress())
				                  .phone(request.getPhone())
				                  .email(request.getEmail())
				                  .admissionDate(request.getAdmissionDate())
				                  .schoolClass(schoolClass)
				                  .section(section)
				                  .parent(parent)
				                  .photo(request.getPhoto())
				                  .status(request.getStatus() != null ? Status.valueOf(request.getStatus())
				                                                      : Status.ACTIVE)
				                  .build();
		
		return StudentResponse.from(studentRepository.save(student));
	}
	
	@Transactional
	public StudentResponse updateStudent(Long schoolId, Long studentId, StudentRequest request) {
		Student student = studentRepository.findByIdAndSchoolId(studentId, schoolId)
				                  .orElseThrow(() -> new ResourceNotFoundException("Student", studentId));
		
		student.setFirstName(request.getFirstName());
		student.setLastName(request.getLastName());
		student.setDob(request.getDob());
		student.setGender(request.getGender() != null ? Gender.valueOf(request.getGender()) : null);
		student.setAddress(request.getAddress());
		student.setPhone(request.getPhone());
		student.setEmail(request.getEmail());
		student.setAdmissionDate(request.getAdmissionDate());
		student.setPhoto(request.getPhoto());
		
		if(request.getStatus() != null){
			student.setStatus(Status.valueOf(request.getStatus()));
		}
		if(request.getClassId() != null){
			student.setSchoolClass(classRepository.findById(request.getClassId())
					                       .orElseThrow(() -> new ResourceNotFoundException("Class",
					                                                                        request.getClassId())));
		}
		if(request.getSectionId() != null){
			student.setSection(sectionRepository.findById(request.getSectionId())
					                   .orElseThrow(() -> new ResourceNotFoundException("Section",
					                                                                    request.getSectionId())));
		}
		if(request.getParentId() != null){
			student.setParent(parentRepository.findById(request.getParentId())
					                  .orElseThrow(() -> new ResourceNotFoundException("Parent", request.getParentId())));
		}
		
		return StudentResponse.from(studentRepository.save(student));
	}
	
	@Transactional
	public void deleteStudent(Long schoolId, Long studentId) {
		Student student = studentRepository.findByIdAndSchoolId(studentId, schoolId)
				                  .orElseThrow(() -> new ResourceNotFoundException("Student", studentId));
		studentRepository.delete(student);
	}
}
