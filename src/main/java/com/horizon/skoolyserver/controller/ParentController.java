package com.horizon.skoolyserver.controller;
import com.horizon.skoolyserver.dto.ParentDTO;
import com.horizon.skoolyserver.service.ParentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parents")
@RequiredArgsConstructor
public class ParentController {
	private final ParentService parentService;
	
	@PostMapping
	public ResponseEntity<ParentDTO> createParent(@RequestBody ParentDTO parentDTO) {
		return ResponseEntity.ok(parentService.createParent(parentDTO));
	}
	
	@GetMapping
	public ResponseEntity<List<ParentDTO>> getAllParents() {
		return ResponseEntity.ok(parentService.findAllParents());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ParentDTO> getParentById(@PathVariable Long id) {
		return ResponseEntity.ok(parentService.getParentById(id));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ParentDTO> updateParentById(@PathVariable Long id, @RequestBody ParentDTO parentDTO) {
		return ResponseEntity.ok(parentService.updateParent(id, parentDTO));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteParentById(@PathVariable Long id) {
		parentService.deleteParentById(id);
		return ResponseEntity.noContent().build();
	}
}
