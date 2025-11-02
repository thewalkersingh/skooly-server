package com.horizon.skoolyserver.repository;
import com.horizon.skoolyserver.model.Parent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParentRepository extends JpaRepository<Parent, Long> {
	Optional<Parent> findByEmail(String email);
	
	Parent findByName(String name);
}