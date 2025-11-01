package com.horizon.skoolyserver.service;
import com.horizon.skoolyserver.dto.ParentDTO;
import com.horizon.skoolyserver.model.Parent;
import com.horizon.skoolyserver.repository.ParentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ParentService {
	private final ParentRepository parentRepository;
	
	public ParentDTO createParent(ParentDTO dto) {
		Parent parent = mapToEntity(dto);
		Parent saved = parentRepository.save(parent);
		return mapToDTO(saved);
	}
	
	private ParentDTO mapToDTO(Parent parent) {
		ParentDTO parentDTO = new ParentDTO();
		parentDTO.setId(parent.getId());
		parentDTO.setName(parent.getName());
		parentDTO.setEmail(parent.getEmail());
		parentDTO.setPhone(parent.getPhone());
		return parentDTO;
	}
	
	private Parent mapToEntity(ParentDTO dto) {
		Parent parent = new Parent();
		parent.setId(dto.getId());
		parent.setName(dto.getName());
		parent.setEmail(dto.getEmail());
		parent.setPhone(dto.getPhone());
		return parent;
	}
	
	public List<ParentDTO> findAllParents() {
		return parentRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
	}
	
	public ParentDTO getParentById(Long id) {
		return parentRepository.findById(id).map(this::mapToDTO)
		                       .orElseThrow(() -> new RuntimeException("Parent not found"));
	}
	
	public ParentDTO updateParent(Long id, ParentDTO dto) {
		Parent parent = parentRepository.findById(id).orElseThrow(() -> new RuntimeException("Parent not found"));
		parent.setName(dto.getName());
		parent.setEmail(dto.getEmail());
		parent.setPhone(dto.getPhone());
		Parent updated = parentRepository.save(parent);
		return mapToDTO(updated);
	}
	
	public void deleteParentById(Long id) {
		parentRepository.deleteById(id);
	}
}
