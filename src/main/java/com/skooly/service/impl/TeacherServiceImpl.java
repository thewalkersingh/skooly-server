package com.skooly.service.impl;
import com.skooly.constant.Gender;
import com.skooly.constant.Status;
import com.skooly.dto.request.TeacherRequest;
import com.skooly.dto.response.TeacherResponse;
import com.skooly.exception.BadRequestException;
import com.skooly.exception.ResourceNotFoundException;
import com.skooly.model.*;
import com.skooly.repository.*;
import com.skooly.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
	private final TeacherRepository teacherRepository;
	private final UserRepository userRepository;
	private final SchoolRepository schoolRepository;
	private final SubjectRepository subjectRepository;
	
	public List<TeacherResponse> getAllTeachers(Long schoolId) {
		return teacherRepository.findBySchoolId(schoolId)
				       .stream().map(TeacherResponse::from).toList();
	}
	
	public TeacherResponse getTeacherById(Long schoolId, Long teacherId) {
		Teacher teacher = teacherRepository.findByIdAndSchoolId(teacherId, schoolId)
				                  .orElseThrow(() -> new ResourceNotFoundException("Teacher", teacherId));
		return TeacherResponse.from(teacher);
	}
	
	public List<TeacherResponse> searchTeachers(Long schoolId, String query) {
		return teacherRepository.searchBySchoolId(schoolId, query)
				       .stream().map(TeacherResponse::from).toList();
	}
	
	public long countTeachers(Long schoolId) {
		return teacherRepository.countBySchoolId(schoolId);
	}
	
	@Transactional
	public TeacherResponse createTeacher(Long schoolId, TeacherRequest request) {
		School school = schoolRepository.findById(schoolId)
				                .orElseThrow(() -> new ResourceNotFoundException("School", schoolId));
		
		if(userRepository.existsBySchoolIdAndUsername(schoolId, request.getUsername())){
			throw new BadRequestException("Username '"+request.getUsername()+"' already exists");
		}
		
		// Create user account atomically
		User user = User.builder()
				            .school(school)
				            .username(request.getUsername())
				            .password(request.getPassword()) // TODO: BCrypt when JWT added
				            .role("TEACHER")
				            .isActive(true)
				            .build();
		user = userRepository.save(user);
		
		Subject subject = null;
		if(request.getSubjectId() != null){
			subject = subjectRepository.findById(request.getSubjectId())
					          .orElseThrow(() -> new ResourceNotFoundException("Subject", request.getSubjectId()));
		}
		
		Teacher teacher = Teacher.builder()
				                  .school(school)
				                  .user(user)
				                  .firstName(request.getFirstName())
				                  .lastName(request.getLastName())
				                  .dob(request.getDob())
				                  .gender(request.getGender() != null ? Gender.valueOf(request.getGender()) : null)
				                  .address(request.getAddress())
				                  .phone(request.getPhone())
				                  .email(request.getEmail())
				                  .joiningDate(request.getJoiningDate())
				                  .subject(subject)
				                  .qualification(request.getQualification())
				                  .experience(request.getExperience())
				                  .photo(request.getPhoto())
				                  .status(request.getStatus() != null
				                          ? Status.valueOf(request.getStatus())
				                          : Status.ACTIVE)
				                  .build();
		
		return TeacherResponse.from(teacherRepository.save(teacher));
	}
	
	@Transactional
	public TeacherResponse updateTeacher(Long schoolId, Long teacherId, TeacherRequest request) {
		Teacher teacher = teacherRepository.findByIdAndSchoolId(teacherId, schoolId)
				                  .orElseThrow(() -> new ResourceNotFoundException("Teacher", teacherId));
		
		teacher.setFirstName(request.getFirstName());
		teacher.setLastName(request.getLastName());
		teacher.setDob(request.getDob());
		teacher.setGender(request.getGender() != null ? Gender.valueOf(request.getGender()) : null);
		teacher.setAddress(request.getAddress());
		teacher.setPhone(request.getPhone());
		teacher.setEmail(request.getEmail());
		teacher.setJoiningDate(request.getJoiningDate());
		teacher.setQualification(request.getQualification());
		teacher.setExperience(request.getExperience());
		teacher.setPhoto(request.getPhoto());
		
		if(request.getStatus() != null){
			teacher.setStatus(Status.valueOf(request.getStatus()));
		}
		if(request.getSubjectId() != null){
			teacher.setSubject(subjectRepository.findById(request.getSubjectId())
					                   .orElseThrow(() -> new ResourceNotFoundException("Subject",
					                                                                    request.getSubjectId())));
		}
		
		return TeacherResponse.from(teacherRepository.save(teacher));
	}
	
	@Transactional
	public void deleteTeacher(Long schoolId, Long teacherId) {
		Teacher teacher = teacherRepository.findByIdAndSchoolId(teacherId, schoolId)
				                  .orElseThrow(() -> new ResourceNotFoundException("Teacher", teacherId));
		teacherRepository.delete(teacher);
	}
}