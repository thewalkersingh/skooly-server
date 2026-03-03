package com.skooly.mapper;
import com.skooly.dto.request.CreateBookRequest;
import com.skooly.dto.response.BookIssueResponse;
import com.skooly.dto.response.BookResponse;
import com.skooly.model.Book;
import com.skooly.model.BookIssue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LibraryMapper {
	BookResponse toBookResponse(Book book);
	
	@Mapping(target = "bookId", source = "book.id")
	@Mapping(target = "bookTitle", source = "book.title")
	@Mapping(target = "studentId", source = "student.id")
	@Mapping(target = "studentName",
			expression = "java(i.getStudent().getFirstName() + ' ' + i.getStudent().getLastName())")
	@Mapping(target = "status", expression = "java(i.getStatus().name())")
	BookIssueResponse toIssueResponse(BookIssue i);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "availableCopies", ignore = true)
	Book toBookEntity(CreateBookRequest request);
}