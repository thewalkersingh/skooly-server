package com.skooly.security;

import com.skooly.entity.User;
import com.skooly.enums.UserStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public class CustomUserDetails implements UserDetails {
	
	// Expose the underlying User entity for use in filters/services
	private final User user;
	
	public CustomUserDetails(User user) {
		this.user = user;
	}
	
	public Long getUserId() {
		return user.getId();
	}
	
	public Long getRoleEntityId() {
		return user.getRoleEntityId();
	}
	
	@Override
	public String getUsername() {
		// Return email if available, otherwise phone
		return user.getIdentity().getEmail() != null
			       ? user.getIdentity().getEmail()
			       : user.getIdentity().getPhone();
	}
	
	@Override
	public String getPassword() {
		return user.getPassword();
	}
	
	@Override
	public List<SimpleGrantedAuthority> getAuthorities() {
		// ROLE_ prefix is Spring Security convention
		return List.of(new SimpleGrantedAuthority("ROLE_" + user.getUserRole().name()));
	}
	
	// ── Account state checks ──────────────────────────────────────────────────
	@Override
	public boolean isAccountNonExpired() {
		return true;   // we handle expiry via UserStatus, not Spring's mechanism
	}
	
	@Override
	public boolean isAccountNonLocked() {
		// Account is locked if status is anything other than ACTIVE
		return switch (user.getUserStatus()) {
			case ACTIVE -> true;
			case PENDING, INACTIVE, REJECTED, DELETED -> false;
		};
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return true;   // we handle this via firstLogin flag + OTP flow
	}
	
	@Override
	public boolean isEnabled() {
		return user.getUserStatus() == UserStatus.ACTIVE;
	}
	
}