package com.horizon.skoolyserver.repository;
import com.horizon.skoolyserver.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
   Optional<User> findByUsername(String username);
   
   boolean existsByUsername(String username);
   
   boolean existsByEmail(String email);
}
