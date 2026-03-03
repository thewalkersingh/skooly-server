package com.skooly.model;// Payroll.java
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "payroll",
		uniqueConstraints = @UniqueConstraint(columnNames = {"staff_id", "month", "year"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payroll extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "staff_id", nullable = false)
	private Staff staff;
	
	@Column(nullable = false)
	private Integer month;
	
	@Column(nullable = false)
	private Integer year;
	
	@Column(name = "basic_salary", nullable = false, precision = 10, scale = 2)
	private BigDecimal basicSalary;
	
	@Column(precision = 10, scale = 2)
	private BigDecimal allowances = BigDecimal.ZERO;
	
	@Column(precision = 10, scale = 2)
	private BigDecimal deductions = BigDecimal.ZERO;
	
	@Column(name = "net_salary", nullable = false, precision = 10, scale = 2)
	private BigDecimal netSalary;
	
	@Column(name = "paid_date")
	private LocalDate paidDate;
}