package com.skooly.mapper;
import com.skooly.dto.request.CreateExamRequest;
import com.skooly.dto.response.ExamResponse;
import com.skooly.dto.response.ResultResponse;
import com.skooly.model.Exam;
import com.skooly.model.Result;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExamMapper {
	@Mapping(target = "classId", source = "schoolClass.id")
	@Mapping(target = "className", source = "schoolClass.name")
	@Mapping(target = "subjectId", source = "subject.id")
	@Mapping(target = "subjectName", source = "subject.name")
	ExamResponse toExamResponse(Exam exam);
	
	@Mapping(target = "examId", source = "exam.id")
	@Mapping(target = "examName", source = "exam.name")
	@Mapping(target = "studentId", source = "student.id")
	@Mapping(target = "studentName",
			expression = "java(r.getStudent().getFirstName() + ' ' + r.getStudent().getLastName())")
	@Mapping(target = "totalMarks", source = "exam.totalMarks")
	@Mapping(target = "status", expression = "java(r.getStatus().name())")
	ResultResponse toResultResponse(Result r);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "schoolClass", ignore = true)
	@Mapping(target = "subject", ignore = true)
	Exam toExamEntity(CreateExamRequest request);
}