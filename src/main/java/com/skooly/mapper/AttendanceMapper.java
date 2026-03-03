package com.skooly.mapper;
import com.skooly.dto.response.AttendanceResponse;
import com.skooly.dto.response.TeacherAttendanceResponse;
import com.skooly.model.Attendance;
import com.skooly.model.TeacherAttendance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AttendanceMapper {
	@Mapping(target = "studentId", source = "student.id")
	@Mapping(target = "studentName",
			expression = "java(a.getStudent().getFirstName() + ' ' + a.getStudent().getLastName())")
	@Mapping(target = "classId", source = "schoolClass.id")
	@Mapping(target = "className", source = "schoolClass.name")
	@Mapping(target = "status", expression = "java(a.getStatus().name())")
	@Mapping(target = "markedBy", expression = "java(a.getMarkedBy() != null ? a.getMarkedBy().getUsername() : null)")
	AttendanceResponse toResponse(Attendance a);
	
	@Mapping(target = "teacherId", source = "teacher.id")
	@Mapping(target = "teacherName",
			expression = "java(a.getTeacher().getFirstName() + ' ' + a.getTeacher().getLastName())")
	@Mapping(target = "status", expression = "java(a.getStatus().name())")
	TeacherAttendanceResponse toTeacherResponse(TeacherAttendance a);
}