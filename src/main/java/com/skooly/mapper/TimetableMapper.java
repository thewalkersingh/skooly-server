package com.skooly.mapper;
import com.skooly.dto.request.CreateTimetableRequest;
import com.skooly.dto.response.TimetableResponse;
import com.skooly.model.Timetable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TimetableMapper {
	@Mapping(target = "classId", source = "schoolClass.id")
	@Mapping(target = "className", source = "schoolClass.name")
	@Mapping(target = "sectionId", source = "section.id")
	@Mapping(target = "sectionName", source = "section.name")
	@Mapping(target = "subjectId", source = "subject.id")
	@Mapping(target = "subjectName", source = "subject.name")
	@Mapping(target = "teacherId", source = "teacher.id")
	@Mapping(target = "teacherName",
			expression = "java(t.getTeacher().getFirstName() + ' ' + t.getTeacher().getLastName())")
	@Mapping(target = "roomId", source = "room.id")
	@Mapping(target = "roomName", source = "room.name")
	@Mapping(target = "dayOfWeek", expression = "java(t.getDayOfWeek().name())")
	TimetableResponse toResponse(Timetable t);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "schoolClass", ignore = true)
	@Mapping(target = "section", ignore = true)
	@Mapping(target = "subject", ignore = true)
	@Mapping(target = "teacher", ignore = true)
	@Mapping(target = "room", ignore = true)
	Timetable toEntity(CreateTimetableRequest request);
}