package com.skooly.model;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "book_issues")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class BookIssue extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "book_id", nullable = false)
	private Book book;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "student_id", nullable = false)
	private Student student;
	
	@Column(name = "issue_date", nullable = false)
	private LocalDate issueDate;
	
	@Column(name = "due_date", nullable = false)
	private LocalDate dueDate;
	
	@Column(name = "return_date")
	private LocalDate returnDate;
	
	@Column(precision = 8, scale = 2)
	private BigDecimal fine = BigDecimal.ZERO;
	
	@Enumerated(EnumType.STRING)
	private IssueStatus status = IssueStatus.ISSUED;
	
	public enum IssueStatus { ISSUED, RETURNED, OVERDUE }
}