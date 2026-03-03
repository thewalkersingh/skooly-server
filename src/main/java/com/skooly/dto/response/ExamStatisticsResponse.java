package com.skooly.dto.response;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamStatisticsResponse {
	private Long examId;
	private String examName;
	private long totalStudents;
	private long passedStudents;
	private long failedStudents;
	private double passPercentage;
	private double averageMarks;
	private String topperName;
	private double topperMarks;
}