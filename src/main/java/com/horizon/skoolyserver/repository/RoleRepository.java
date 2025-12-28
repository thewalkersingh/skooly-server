package com.horizon.skoolyserver.repository;
import com.horizon.skoolyserver.constants.RoleName;
import com.horizon.skoolyserver.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
   Optional<Role> findByName(RoleName name);
}
