package com.skooly.dto.request;
import com.skooly.model.Facility;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateFacilityRequest {
	@NotBlank(message = "Facility name is required")
	@Size(max = 100)
	private String name;
	private String description;
	
	@Size(max = 255)
	private String location;
	private Facility.FacilityStatus status = Facility.FacilityStatus.ACTIVE;
}