package com.skooly.dto.response;
import com.skooly.model.Book;
import lombok.Data;

@Data
public class BookResponse {
	private Long id;
	private String title;
	private String author;
	private String isbn;
	private String category;
	private Integer totalCopies;
	private Integer availableCopies;
	private String publisher;
	private Integer publishedYear;
	
	public static BookResponse from(Book b) {
		BookResponse r = new BookResponse();
		r.setId(b.getId());
		r.setTitle(b.getTitle());
		r.setAuthor(b.getAuthor());
		r.setIsbn(b.getIsbn());
		r.setCategory(b.getCategory());
		r.setTotalCopies(b.getTotalCopies());
		r.setAvailableCopies(b.getAvailableCopies());
		r.setPublisher(b.getPublisher());
		r.setPublishedYear(b.getPublishedYear());
		return r;
	}
	
}