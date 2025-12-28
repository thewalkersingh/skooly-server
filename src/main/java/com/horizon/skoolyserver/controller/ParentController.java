package com.horizon.skoolyserver.controller;
import com.horizon.skoolyserver.dto.parent.ParentDTO;
import com.horizon.skoolyserver.service.ParentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parents")
@RequiredArgsConstructor
public class ParentController {
	private final ParentService parentService;
	
	@PostMapping
	public ResponseEntity<ParentDTO> createParent(@Valid @RequestBody ParentDTO parentDTO) {
		ParentDTO created = parentService.createParent(parentDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
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
	public ResponseEntity<ParentDTO> updateParentById(@PathVariable Long id, @Valid @RequestBody ParentDTO parentDTO) {
		return ResponseEntity.ok(parentService.updateParent(id, parentDTO));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteParentById(@PathVariable Long id) {
		parentService.deleteParentById(id);
		return ResponseEntity.noContent().build();
	}
}
