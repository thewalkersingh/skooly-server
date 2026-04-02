package com.skooly.dto.response;
import com.skooly.model.BookIssue;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BookIssueResponse {
	private Long id;
	private String status;
	private LocalDate issueDate;
	private LocalDate dueDate;
	private LocalDate returnDate;
	private BigDecimal fine;
	private Long bookId;
	private String bookTitle;
	private String bookAuthor;
	private Long studentId;
	private String studentName;
	
	public static BookIssueResponse from(BookIssue bi) {
		BookIssueResponse r = new BookIssueResponse();
		r.setId(bi.getId());
		r.setStatus(bi.getStatus().name());
		r.setIssueDate(bi.getIssueDate());
		r.setDueDate(bi.getDueDate());
		r.setReturnDate(bi.getReturnDate());
		r.setFine(bi.getFine());
		if(bi.getBook() != null){
			r.setBookId(bi.getBook().getId());
			r.setBookTitle(bi.getBook().getTitle());
			r.setBookAuthor(bi.getBook().getAuthor());
		}
		if(bi.getStudent() != null){
			r.setStudentId(bi.getStudent().getId());
			r.setStudentName(bi.getStudent().getFirstName() + " " + bi.getStudent().getLastName());
		}
		return r;
	}
	
}