package com.skooly.dto.request;
import com.skooly.model.Room;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRoomRequest {
	@NotBlank(message = "Room name is required")
	@Size(max = 100)
	private String name;
	private Room.RoomType type;
	
	@Min(1)
	private Integer capacity;
	
	@Size(max = 50)
	private String floor;
	
	@Size(max = 100)
	private String building;
	private Room.Status status = Room.Status.AVAILABLE;
}