package com.horizon.skoolyserver.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.horizon.skoolyserver.constants.AttendanceStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "attendances")
public class Attendance {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDate date;
	
	@Enumerated(EnumType.STRING)
	private AttendanceStatus status;
	
	@ManyToOne
	@JoinColumn(name = "student_id")
	@JsonBackReference
	private Student student;

}