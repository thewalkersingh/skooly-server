package com.skooly.service;
import com.skooly.dto.request.BulkTimetableRequest;
import com.skooly.dto.request.CreateTimetableRequest;
import com.skooly.dto.response.TimetableResponse;
import com.skooly.model.Timetable;

import java.util.List;

public interface TimetableService {
	List<TimetableResponse> getAllTimetables();
	
	TimetableResponse getTimetableById(Long id);
	
	TimetableResponse createTimetable(CreateTimetableRequest request);
	
	List<TimetableResponse> createBulkTimetable(BulkTimetableRequest request);
	
	TimetableResponse updateTimetable(Long id, CreateTimetableRequest request);
	
	void deleteTimetable(Long id);
	
	List<TimetableResponse> getByClassAndSection(Long classId, Long sectionId);
	
	List<TimetableResponse> getByTeacher(Long teacherId);
	
	List<TimetableResponse> getByDay(Timetable.DayOfWeek day);
}