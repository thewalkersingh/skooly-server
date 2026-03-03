package com.skooly.dto.request;
import com.skooly.model.Parent;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateParentRequest {
	@NotNull(message = "User ID is required")
	private Long userId;
	
	@NotBlank(message = "First name is required")
	@Size(max = 100)
	private String firstName;
	
	@NotBlank(message = "Last name is required")
	@Size(max = 100)
	private String lastName;
	
	@Size(max = 20)
	private String phone;
	
	@Email
	private String email;
	private String address;
	
	@Size(max = 150)
	private String occupation;
	
	@NotNull(message = "Relation is required")
	private Parent.Relation relation;
}