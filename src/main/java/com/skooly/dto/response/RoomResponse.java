package com.skooly.dto.response;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomResponse {
	private Long id;
	private String name;
	private String type;
	private Integer capacity;
	private String floor;
	private String building;
	private String status;
	private LocalDateTime createdAt;
}