package com.skooly.service.impl;
import com.skooly.dto.request.BulkTimetableRequest;
import com.skooly.dto.request.CreateTimetableRequest;
import com.skooly.dto.response.TimetableResponse;
import com.skooly.exception.BadRequestException;
import com.skooly.exception.ResourceNotFoundException;
import com.skooly.mapper.TimetableMapper;
import com.skooly.model.Timetable;
import com.skooly.repository.*;
import com.skooly.service.TimetableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TimetableServiceImpl implements TimetableService {
	private final TimetableRepository timetableRepository;
	private final SchoolClassRepository classRepository;
	private final SectionRepository sectionRepository;
	private final SubjectRepository subjectRepository;
	private final TeacherRepository teacherRepository;
	private final RoomRepository roomRepository;
	private final TimetableMapper timetableMapper;
	
	@Override
	@Transactional(readOnly = true)
	public List<TimetableResponse> getAllTimetables() {
		return timetableRepository.findAll().stream().map(timetableMapper::toResponse).toList();
	}
	
	@Override
	@Transactional(readOnly = true)
	public TimetableResponse getTimetableById(Long id) {
		return timetableMapper.toResponse(findTimetableById(id));
	}
	
	@Override
	public TimetableResponse createTimetable(CreateTimetableRequest request) {
		validateAndBuild(request, null);
		Timetable timetable = buildTimetable(request);
		return timetableMapper.toResponse(timetableRepository.save(timetable));
	}
	
	@Override
	public List<TimetableResponse> createBulkTimetable(BulkTimetableRequest request) {
		return request.getEntries().stream().map(entry -> {
			validateAndBuild(entry, null);
			return timetableMapper.toResponse(timetableRepository.save(buildTimetable(entry)));
		}).toList();
	}
	
	@Override
	public TimetableResponse updateTimetable(Long id, CreateTimetableRequest request) {
		findTimetableById(id);
		validateAndBuild(request, id);
		Timetable timetable = buildTimetable(request);
		timetable.setId(id);
		return timetableMapper.toResponse(timetableRepository.save(timetable));
	}
	
	@Override
	public void deleteTimetable(Long id) {
		if(!timetableRepository.existsById(id)){
			throw new ResourceNotFoundException("Timetable entry not found with id: "+id);
		}
		timetableRepository.deleteById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<TimetableResponse> getByClassAndSection(Long classId, Long sectionId) {
		return timetableRepository.findBySchoolClassIdAndSectionId(classId, sectionId)
				       .stream().map(timetableMapper::toResponse).toList();
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<TimetableResponse> getByTeacher(Long teacherId) {
		return timetableRepository.findByTeacherId(teacherId)
				       .stream().map(timetableMapper::toResponse).toList();
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<TimetableResponse> getByDay(Timetable.DayOfWeek day) {
		return timetableRepository.findByDayOfWeek(day)
				       .stream().map(timetableMapper::toResponse).toList();
	}
	
	// ── Private helpers ──────────────────────────────────────────────────────
	
	private void validateAndBuild(CreateTimetableRequest request, Long excludeId) {
		Long safeExcludeId = excludeId != null ? excludeId : -1L;
		
		if(!timetableRepository.findTeacherConflicts(
				request.getTeacherId(), request.getDayOfWeek(),
				request.getStartTime(), request.getEndTime(), safeExcludeId).isEmpty()){
			throw new BadRequestException("Teacher has a conflicting timetable slot");
		}
		
		if(request.getRoomId() != null &&
		   !timetableRepository.findRoomConflicts(
				   request.getRoomId(), request.getDayOfWeek(),
				   request.getStartTime(), request.getEndTime(), safeExcludeId).isEmpty()){
			throw new BadRequestException("Room has a conflicting timetable slot");
		}
	}
	
	private Timetable buildTimetable(CreateTimetableRequest request) {
		Timetable timetable = timetableMapper.toEntity(request);
		timetable.setSchoolClass(classRepository.findById(request.getClassId())
				                         .orElseThrow(() -> new ResourceNotFoundException(
						                         "Class not found with id: "+request.getClassId())));
		timetable.setSection(sectionRepository.findById(request.getSectionId())
				                     .orElseThrow(() -> new ResourceNotFoundException(
						                     "Section not found with id: "+request.getSectionId())));
		timetable.setSubject(subjectRepository.findById(request.getSubjectId())
				                     .orElseThrow(() -> new ResourceNotFoundException(
						                     "Subject not found with id: "+request.getSubjectId())));
		timetable.setTeacher(teacherRepository.findById(request.getTeacherId())
				                     .orElseThrow(() -> new ResourceNotFoundException(
						                     "Teacher not found with id: "+request.getTeacherId())));
		if(request.getRoomId() != null){
			timetable.setRoom(roomRepository.findById(request.getRoomId())
					                  .orElseThrow(() -> new ResourceNotFoundException(
							                  "Room not found with id: "+request.getRoomId())));
		}
		return timetable;
	}
	
	private Timetable findTimetableById(Long id) {
		return timetableRepository.findById(id)
				       .orElseThrow(() -> new ResourceNotFoundException("Timetable entry not found with id: "+id));
	}
}