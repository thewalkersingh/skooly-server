package com.horizon.skoolyserver.repository;
import com.horizon.skoolyserver.model.Parent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentRepository extends JpaRepository<Parent, Long> {
	Parent findByEmail(String email);
	
	Parent findByName(String name);
}