package com.skooly.dto.response;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportCardResponse {
	private Long studentId;
	private String studentName;
	private String className;
	private String sectionName;
	private String academicYear;
	private List<ResultResponse> results;
	private double overallPercentage;
	private String overallGrade;
	private int rank;
}