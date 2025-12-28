package com.horizon.skoolyserver.controller;
import com.horizon.skoolyserver.constants.RoleName;
import com.horizon.skoolyserver.dto.auth.AuthResponse;
import com.horizon.skoolyserver.dto.auth.LoginRequest;
import com.horizon.skoolyserver.dto.auth.SignupRequest;
import com.horizon.skoolyserver.model.Role;
import com.horizon.skoolyserver.model.User;
import com.horizon.skoolyserver.repository.RoleRepository;
import com.horizon.skoolyserver.repository.UserRepository;
import com.horizon.skoolyserver.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
   private final AuthenticationManager authenticationManager;
   private final UserRepository userRepository;
   private final RoleRepository roleRepository;
   private final PasswordEncoder passwordEncoder;
   private final JwtUtils jwtUtils;
   
   @PostMapping("/login")
   public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
	  Authentication auth = authenticationManager.authenticate(
			  new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
	  UserDetails userDetails = (UserDetails) auth.getPrincipal();
	  String token = jwtUtils.generateToken(userDetails);
	  List<String> roles = userDetails.getAuthorities().stream()
								   .map(GrantedAuthority::getAuthority).toList();
	  return ResponseEntity.ok(new AuthResponse(token, userDetails.getUsername(), roles));
   }
   
   @PostMapping("/register")
   public ResponseEntity<String> register(@RequestBody SignupRequest request) {
	  if(userRepository.existsByUsername(request.getUsername())){
		 return ResponseEntity.badRequest().body("Username already exists");
	  }
	  
	  User user = new User();
	  user.setUsername(request.getUsername());
	  user.setEmail(request.getEmail());
	  user.setPassword(passwordEncoder.encode(request.getPassword()));
	  
	  Set<Role> roles = new HashSet<>();
	  for(String roleStr : request.getRoles()){
		 RoleName roleName = RoleName.valueOf("ROLE_" + roleStr.toUpperCase());
		 Role role = roleRepository.findByName(roleName)
							 .orElseThrow(() -> new RuntimeException("Role not found"));
		 roles.add(role);
	  }
	  
	  user.setRoles(roles);
	  userRepository.save(user);
	  return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
   }
}