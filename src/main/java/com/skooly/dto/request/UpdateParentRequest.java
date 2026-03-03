package com.skooly.dto.request;
import com.skooly.model.Parent;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateParentRequest {
	@Size(max = 100)
	private String firstName;
	
	@Size(max = 100)
	private String lastName;
	
	@Size(max = 20)
	private String phone;
	
	@Email
	private String email;
	private String address;
	
	@Size(max = 150)
	private String occupation;
	private Parent.Relation relation;
}