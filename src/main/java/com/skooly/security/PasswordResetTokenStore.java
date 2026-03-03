package com.skooly.security;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PasswordResetTokenStore {
	private static final long TOKEN_EXPIRY_MINUTES = 30;
	private final Map<String, TokenEntry> store = new ConcurrentHashMap<>();
	
	public String generateToken(String username) {
		String token = UUID.randomUUID().toString();
		store.put(token, new TokenEntry(username, LocalDateTime.now().plusMinutes(TOKEN_EXPIRY_MINUTES)));
		return token;
	}
	
	public String getUsernameByToken(String token) {
		TokenEntry entry = store.get(token);
		if(entry == null || entry.expiry().isBefore(LocalDateTime.now())){
			return null;
		}
		return entry.username();
	}
	
	public void invalidateToken(String token) {
		store.remove(token);
	}
	
	private record TokenEntry(String username, LocalDateTime expiry) {
	}
}