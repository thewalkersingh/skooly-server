package com.skooly.service.impl;

import com.skooly.dto.request.SectionRequest;
import com.skooly.dto.response.SectionResponse;
import com.skooly.entity.Classroom;
import com.skooly.entity.Section;
import com.skooly.entity.Subject;
import com.skooly.entity.Teacher;
import com.skooly.exception.ResourceNotFoundException;
import com.skooly.mapper.SectionMapper;
import com.skooly.repository.*;
import com.skooly.service.SectionService;
import com.skooly.wrapper.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SectionServiceImpl implements SectionService {
	
	private final SectionRepository sectionRepository;
	private final SectionMapper sectionMapper;
	private final ClassroomRepository classroomRepository;
	private final TeacherRepository teacherRepository;
	private final SubjectRepository subjectRepository;
	private final SchoolRepository schoolRepository;
	
	// ── Create / Update / Delete ──────────────────────────────────────────────
	public SectionResponse createSection(Long classroomId, SectionRequest request) {
		Section section = sectionMapper.toEntity(request);
		Classroom classroom =
			classroomRepository.findById(classroomId).orElseThrow(() -> new RuntimeException("Classroom not found"));
		section.setClassroom(classroom);
		
		if (request.getTeacherId() != null) {
			Teacher teacher = teacherRepository.findById(request.getTeacherId())
				                  .orElseThrow(() -> new RuntimeException("Teacher not found"));
			section.setTeacher(teacher);
		}
		if (request.getSubjectIds() != null) {
			List<Subject> subjects = subjectRepository.findAllById(request.getSubjectIds());
			section.setSubjects(subjects);
		}
		section = sectionRepository.save(section);
		return sectionMapper.toResponse(section);
	}
	
	public SectionResponse updateSection(Long sectionId, SectionRequest request) {
		Section section =
			sectionRepository.findById(sectionId).orElseThrow(() -> new ResourceNotFoundException("Section not found"));
		Section updateRequest = sectionMapper.toEntity(request);
		section.setClassroom(updateRequest.getClassroom());
		if (request.getTeacherId() != null) {
			section.setTeacher(updateRequest.getTeacher());
		}
		if (request.getSubjectIds() != null) {
			List<Subject> subjects = subjectRepository.findAllById(request.getSubjectIds());
			section.setSubjects(subjects);
		}
		
		section = sectionRepository.save(section);
		return sectionMapper.toResponse(section);
	}
	
	public void deleteSection(Long sectionId) {
		sectionRepository.deleteById(sectionId);
	}
	
	// ── Single fetch ──────────────────────────────────────────────────────────
	public SectionResponse getSection(Long id) {
		return sectionRepository.findById(id)
			       .map(sectionMapper::toResponse)
			       .orElseThrow(() -> new RuntimeException("Section not found"));
	}
	
	@GetMapping("/{id}/with-subjects")
	public SectionResponse getSectionWithSubjects(Long sectionId) {
		Optional<Section> response = sectionRepository.findByIdWithSubjects(sectionId);
		if (response.isPresent()) {
			return sectionMapper.toResponse(response.get());
		} else {
			throw new ResourceNotFoundException("Section not found with id " + sectionId);
		}
	}
	
	// ── Lists ─────────────────────────────────────────────────────────────────
	
	public List<SectionResponse> getSectionsByClassroom(Long classroomId) {
		if (!classroomRepository.existsById(classroomId)) {
			throw new ResourceNotFoundException("Classroom not found");
		}
		return sectionRepository.findByClassroomId(classroomId).stream().map(sectionMapper::toResponse).toList();
	}
	
	public PageResponse<SectionResponse> getSectionsByClassroom(Long classroomId, Pageable pageable) {
		Page<Section> page = sectionRepository.findByClassroomId(classroomId, pageable);
		return PageResponse.<SectionResponse>builder()
			       .data(page.getContent().stream().map(sectionMapper::toResponse).toList())
			       .page(page.getNumber())
			       .size(page.getSize())
			       .totalElements(page.getTotalElements())
			       .totalPages(page.getTotalPages())
			       .hasNext(page.hasNext())
			       .hasPrevious(page.hasPrevious())
			       .build();
	}
	
	public PageResponse<SectionResponse> getSectionsBySchool(Long schoolId, Pageable pageable) {
		if (!schoolRepository.existsById(schoolId)) {
			throw new ResourceNotFoundException("School not found");
		}
		Page<Section> page = sectionRepository.findBySchoolId(schoolId, pageable);
		List<SectionResponse> sectionResponseList = page
			                                            .getContent()
			                                            .stream()
			                                            .map(sectionMapper::toResponse).toList();
		return PageResponse.<SectionResponse>builder()
			       .data(sectionResponseList)
			       .page(page.getNumber())           // current page index (0-based)
			       .size(page.getSize())             // items per page
			       .totalElements(page.getTotalElements())  // total matching records in DB
			       .totalPages(page.getTotalPages()) // total pages available
			       .hasNext(page.hasNext())          // is there a next page?
			       .hasPrevious(page.hasPrevious())  // is there a previous page?
			       .build();
	}
	
	public List<SectionResponse> getSectionsByTeacher(Long teacherId) {
		return List.of();
		// TODO: implement this method and also controller
	}
	
	public List<SectionResponse> getSectionsByClassroomWithSubjects(Long classroomId) {
		if (!classroomRepository.existsById(classroomId)) {
			throw new ResourceNotFoundException("Classroom not found with id " + classroomId);
		}
		List<Section> sectionList = sectionRepository.findByClassroomIdWithSubjects(classroomId);
		return sectionList.
			       stream()
			       .map(sectionMapper::toResponse)
			       .toList();
	}
	
	// ── Teacher assignment ────────────────────────────────────────────────────
	public SectionResponse assignTeacher(Long sectionId, Long teacherId) {
		Section section = sectionRepository
			                  .findById(sectionId)
			                  .orElseThrow(() -> new ResourceNotFoundException("Section not found"));
		Teacher teacher = teacherRepository
			                  .findById(teacherId)
			                  .orElseThrow(() -> new RuntimeException("Teacher not found"));
		section.setTeacher(teacher);
		Section savedSection = sectionRepository.save(section);
		return sectionMapper.toResponse(savedSection);
	}
	
	public SectionResponse removeTeacher(Long sectionId) {
		Section section = sectionRepository
			                  .findById(sectionId)
			                  .orElseThrow(() -> new ResourceNotFoundException("Section not found"));
		section.setTeacher(null);
		sectionRepository.save(section);
		return sectionMapper.toResponse(section);
	}
	
	// ── Subject assignment ────────────────────────────────────────────────────
	public SectionResponse addSubject(Long sectionId, Long subjectId) {
		Section section = sectionRepository
			                  .findById(sectionId)
			                  .orElseThrow(() -> new ResourceNotFoundException("Section not found"));
		Subject subject = subjectRepository.
			                  findById(subjectId)
			                  .orElseThrow(() -> new RuntimeException("Subject not found"));
		section.getSubjects().add(subject);
		sectionRepository.save(section);
		return sectionMapper.toResponse(section);
	}
	
	public SectionResponse removeSubject(Long sectionId, Long subjectId) {
		Section section = sectionRepository.
			                  findById(sectionId)
			                  .orElseThrow(() -> new ResourceNotFoundException("Section not found"));
		Subject subject = subjectRepository.findById(subjectId)
			                  .orElseThrow(() -> new RuntimeException("Subject not found"));
		section.getSubjects().remove(subject);
		sectionRepository.save(section);
		return sectionMapper.toResponse(section);
	}
	
	// ── Unassigned sections (admin utility) ───────────────────────────────────
	public List<SectionResponse> getUnassignedSections(Long schoolId) {
		if (!schoolRepository.existsById(schoolId)) {
			throw new ResourceNotFoundException("School not found");
		}
		return sectionRepository.findUnassignedSectionsBySchoolId(schoolId)
			       .stream()
			       .map(sectionMapper::toResponse)
			       .toList();
	}
	
}