package com.skooly.security;

import com.skooly.entity.User;
import com.skooly.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
	
	private final UserRepository userRepository;
	
	// Spring Security calls this during authentication
	// username here is email OR phone — whichever the user logs in with
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByIdentityEmailOrIdentityPhone(username, username)
		                          .orElseThrow(() -> new UsernameNotFoundException(
			                          "User not found with email or phone: " + username));
//		/ Log authorities here
		log.info("Authorities for {}: {}", username, user.getRole());
		return new CustomUserDetails(user);
	}
	
}