
package com.skooly.service.impl;
import com.skooly.dto.common.PageResponse;
import com.skooly.dto.request.CreateTeacherRequest;
import com.skooly.dto.request.UpdateTeacherRequest;
import com.skooly.dto.response.TeacherResponse;
import com.skooly.dto.response.TeacherSummaryResponse;
import com.skooly.exception.BadRequestException;
import com.skooly.exception.ResourceNotFoundException;
import com.skooly.mapper.TeacherMapper;
import com.skooly.model.Subject;
import com.skooly.model.Teacher;
import com.skooly.model.User;
import com.skooly.repository.TeacherRepository;
import com.skooly.repository.UserRepository;
import com.skooly.service.TeacherService;
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
public class TeacherServiceImpl implements TeacherService {
	private final TeacherRepository teacherRepository;
	private final UserRepository userRepository;
	private final SubjectRepository subjectRepository;
	private final TeacherMapper teacherMapper;
	private final FileUploadUtils fileUploadUtils;
	
	@Override
	@Transactional(readOnly = true)
	public PageResponse<TeacherSummaryResponse> getAllTeachers(
			int page, int size, String search,
			Long subjectId, Teacher.Status status, Teacher.Gender gender) {
		
		Pageable pageable = PageRequest.of(page-1, size, Sort.by("firstName").ascending());
		
		Page<Teacher> teachers = teacherRepository.findWithFilters(
				subjectId, status, gender, search, pageable
		                                                          );
		
		List<TeacherSummaryResponse> data = teachers.getContent()
				                                    .stream()
				                                    .map(teacherMapper::toSummaryResponse)
				                                    .toList();
		
		return new PageResponse<>(data, page, size, teachers.getTotalElements(), teachers.getTotalPages());
	}
	
	@Override
	@Transactional(readOnly = true)
	public TeacherResponse getTeacherById(Long id) {
		return teacherMapper.toResponse(findTeacherById(id));
	}
	
	@Override
	@Transactional(readOnly = true)
	public TeacherResponse getMyProfile(Long userId) {
		Teacher teacher = teacherRepository.findByUserId(userId)
				                  .orElseThrow(() -> new ResourceNotFoundException("Teacher profile not found"));
		return teacherMapper.toResponse(teacher);
	}
	
	@Override
	public TeacherResponse updateMyProfile(Long userId, UpdateTeacherRequest request) {
		Teacher teacher = teacherRepository.findByUserId(userId)
				                  .orElseThrow(() -> new ResourceNotFoundException("Teacher profile not found"));
		applyUpdates(teacher, request);
		return teacherMapper.toResponse(teacherRepository.save(teacher));
	}
	
	@Override
	public TeacherResponse createTeacher(CreateTeacherRequest request) {
		if(request.getEmail() != null && teacherRepository.existsByEmail(request.getEmail())){
			throw new BadRequestException("Email already in use");
		}
		
		User user = userRepository.findById(request.getUserId())
				            .orElseThrow(
						            () -> new ResourceNotFoundException("User not found with id: "+request.getUserId()));
		
		Teacher teacher = teacherMapper.toEntity(request);
		teacher.setUser(user);
		teacher.setStatus(Teacher.Status.ACTIVE);
		
		if(request.getSubjectId() != null){
			teacher.setSubject(findSubjectById(request.getSubjectId()));
		}
		
		return teacherMapper.toResponse(teacherRepository.save(teacher));
	}
	
	@Override
	public TeacherResponse updateTeacher(Long id, UpdateTeacherRequest request) {
		Teacher teacher = findTeacherById(id);
		applyUpdates(teacher, request);
		return teacherMapper.toResponse(teacherRepository.save(teacher));
	}
	
	@Override
	public void deleteTeacher(Long id) {
		if(!teacherRepository.existsById(id)){
			throw new ResourceNotFoundException("Teacher not found with id: "+id);
		}
		teacherRepository.deleteById(id);
	}
	
	@Override
	public void updateStatus(Long id, Teacher.Status status) {
		Teacher teacher = findTeacherById(id);
		teacher.setStatus(status);
		teacherRepository.save(teacher);
	}
	
	@Override
	public TeacherResponse uploadPhoto(Long id, MultipartFile file) {
		Teacher teacher = findTeacherById(id);
		if(teacher.getPhoto() != null){
			fileUploadUtils.deleteFile(teacher.getPhoto());
		}
		teacher.setPhoto(fileUploadUtils.uploadFile(file, "teachers"));
		return teacherMapper.toResponse(teacherRepository.save(teacher));
	}
	
	@Override
	public void deletePhoto(Long id) {
		Teacher teacher = findTeacherById(id);
		if(teacher.getPhoto() != null){
			fileUploadUtils.deleteFile(teacher.getPhoto());
			teacher.setPhoto(null);
			teacherRepository.save(teacher);
		}
	}
	
	// ── Private helpers ──────────────────────────────────────────────────────
	
	private Teacher findTeacherById(Long id) {
		return teacherRepository.findById(id)
				       .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: "+id));
	}
	
	private Subject findSubjectById(Long id) {
		return subjectRepository.findById(id)
				       .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: "+id));
	}
	
	private void applyUpdates(Teacher teacher, UpdateTeacherRequest request) {
		if(request.getFirstName() != null)
			teacher.setFirstName(request.getFirstName());
		if(request.getLastName() != null)
			teacher.setLastName(request.getLastName());
		if(request.getDob() != null)
			teacher.setDob(request.getDob());
		if(request.getGender() != null)
			teacher.setGender(request.getGender());
		if(request.getAddress() != null)
			teacher.setAddress(request.getAddress());
		if(request.getPhone() != null)
			teacher.setPhone(request.getPhone());
		if(request.getEmail() != null)
			teacher.setEmail(request.getEmail());
		if(request.getJoiningDate() != null)
			teacher.setJoiningDate(request.getJoiningDate());
		if(request.getQualification() != null)
			teacher.setQualification(request.getQualification());
		if(request.getExperience() != null)
			teacher.setExperience(request.getExperience());
		if(request.getSubjectId() != null)
			teacher.setSubject(findSubjectById(request.getSubjectId()));
	}
}