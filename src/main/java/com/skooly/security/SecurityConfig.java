package com.skooly.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity          // enables @PreAuthorize on controllers
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final JwtAuthFilter jwtAuthFilter;
	private final UserDetailsServiceImpl userDetailsService;
	
	@Value("${cors.allowed-origins}")
	private String allowedOrigins;
	
	// ── Security Filter Chain ─────────────────────────────────────────────────
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http
			.csrf(AbstractHttpConfigurer::disable)
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.sessionManagement(session ->
				                   session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			
			.authorizeHttpRequests(auth -> auth
				                               
				                               // ── Swagger ──────────────────────────────────────────────────
				                               .requestMatchers(
					                               "/swagger-ui/**",
					                               "/swagger-ui.html",
					                               "/swagger-ui/index.html",
					                               "/api-docs/**",
					                               "/v3/api-docs/**",
					                               "/v3/api-docs",
					                               "/swagger-resources/**",
					                               "/webjars/**"
				                               ).permitAll()
				                               
				                               // ── Public auth endpoints (no token needed) ──────────────────
				                               .requestMatchers(
					                               "/v1/auth/login",
					                               "/v1/auth/register",
					                               "/v1/auth/refresh",
					                               "/v1/auth/forgot-password",
					                               "/v1/auth/verify-otp",
					                               "/v1/auth/resend-otp",
					                               "/v1/auth/reset-password"
				                               ).permitAll()
				                               
				                               // ── Authenticated auth endpoints ──────────────────────────────
				                               .requestMatchers(
					                               "/v1/auth/me",
					                               "/v1/auth/logout"
				                               ).authenticated()
				                               
				                               // ── Admin-only auth actions ───────────────────────────────────
				                               .requestMatchers(
					                               "/v1/auth/create-account",
					                               "/v1/auth/approve/**",
					                               "/v1/auth/reject/**"
				                               ).hasRole("ADMIN")
				                               
				                               // ── School management ─────────────────────────────────────────
				                               .requestMatchers(HttpMethod.GET, "/v1/schools/**").authenticated()
				                               .requestMatchers(HttpMethod.POST, "/v1/schools/**").hasRole("ADMIN")
				                               .requestMatchers(HttpMethod.PUT, "/v1/schools/**").hasRole("ADMIN")
				                               .requestMatchers(HttpMethod.PATCH, "/v1/schools/**").hasRole("ADMIN")
				                               .requestMatchers(HttpMethod.DELETE, "/v1/schools/**").hasRole("ADMIN")
				                               
				                               // ── Classroom management ──────────────────────────────────────
				                               .requestMatchers(HttpMethod.GET, "/v1/classrooms/**").authenticated()
				                               .requestMatchers(HttpMethod.POST, "/v1/classrooms/**").hasRole("ADMIN")
				                               .requestMatchers(HttpMethod.PUT, "/v1/classrooms/**").hasRole("ADMIN")
				                               .requestMatchers(HttpMethod.PATCH, "/v1/classrooms/**").hasRole("ADMIN")
				                               .requestMatchers(HttpMethod.DELETE, "/v1/classrooms/**").hasRole("ADMIN")
				                               
				                               // ── Section management ────────────────────────────────────────
				                               .requestMatchers(HttpMethod.GET, "/v1/sections/**").authenticated()
				                               .requestMatchers(HttpMethod.POST, "/v1/sections/**").hasRole("ADMIN")
				                               .requestMatchers(HttpMethod.PUT, "/v1/sections/**").hasRole("ADMIN")
				                               .requestMatchers(HttpMethod.PATCH, "/v1/sections/**").hasRole("ADMIN")
				                               .requestMatchers(HttpMethod.DELETE, "/v1/sections/**").hasRole("ADMIN")
				                               
				                               // ── Subject management ────────────────────────────────────────
				                               .requestMatchers(HttpMethod.GET, "/v1/subjects/**").authenticated()
				                               .requestMatchers(HttpMethod.POST, "/v1/subjects/**").hasRole("ADMIN")
				                               .requestMatchers(HttpMethod.PUT, "/v1/subjects/**").hasRole("ADMIN")
				                               .requestMatchers(HttpMethod.PATCH, "/v1/subjects/**").hasRole("ADMIN")
				                               .requestMatchers(HttpMethod.DELETE, "/v1/subjects/**").hasRole("ADMIN")
				                               
				                               // ── Teacher management ────────────────────────────────────────
				                               .requestMatchers(HttpMethod.GET, "/v1/teachers/**").authenticated()
				                               .requestMatchers(HttpMethod.POST, "/v1/teachers/**").hasRole("ADMIN")
				                               .requestMatchers(HttpMethod.PUT, "/v1/teachers/**").hasRole("ADMIN")
				                               .requestMatchers(HttpMethod.PATCH, "/v1/teachers/**").hasRole("ADMIN")
				                               .requestMatchers(HttpMethod.DELETE, "/v1/teachers/**").hasRole("ADMIN")
				                               
				                               // ── Student management ────────────────────────────────────────
				                               .requestMatchers(HttpMethod.GET, "/v1/students/**").authenticated()
				                               .requestMatchers(HttpMethod.POST, "/v1/students/**").hasRole("ADMIN")
				                               .requestMatchers(HttpMethod.PUT, "/v1/students/**").hasRole("ADMIN")
				                               .requestMatchers(HttpMethod.PATCH, "/v1/students/**").hasRole("ADMIN")
				                               .requestMatchers(HttpMethod.DELETE, "/v1/students/**").hasRole("ADMIN")
				                               
				                               // ── Parent management ─────────────────────────────────────────
				                               .requestMatchers(HttpMethod.GET, "/v1/parents/**").authenticated()
				                               .requestMatchers(HttpMethod.POST, "/v1/parents/**").hasRole("ADMIN")
				                               .requestMatchers(HttpMethod.PUT, "/v1/parents/**").hasRole("ADMIN")
				                               .requestMatchers(HttpMethod.PATCH, "/v1/parents/**").hasRole("ADMIN")
				                               .requestMatchers(HttpMethod.DELETE, "/v1/parents/**").hasRole("ADMIN")
				                               
				                               // ── Staff management ──────────────────────────────────────────
				                               .requestMatchers(HttpMethod.GET, "/v1/staff/**").authenticated()
				                               .requestMatchers(HttpMethod.POST, "/v1/staff/**").hasRole("ADMIN")
				                               .requestMatchers(HttpMethod.PUT, "/v1/staff/**").hasRole("ADMIN")
				                               .requestMatchers(HttpMethod.PATCH, "/v1/staff/**").hasRole("ADMIN")
				                               .requestMatchers(HttpMethod.DELETE, "/v1/staff/**").hasRole("ADMIN")
				                               
				                               // ── Notifications ─────────────────────────────────────────────
				                               .requestMatchers("/v1/notifications/**").authenticated()
				                               
				                               // ── Everything else — deny ────────────────────────────────────
				                               .anyRequest().denyAll()
			)
			
			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
			.authenticationProvider(authenticationProvider());
		
		return http.build();
	}
	
	// ── CORS ──────────────────────────────────────────────────────────────────
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		
		CorsConfiguration config = new CorsConfiguration();
		
		// Split by comma — supports multiple origins
		List<String> origins = List.of(allowedOrigins.split(","));
		config.setAllowedOrigins(origins);   // http://localhost:5173
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
		config.setExposedHeaders(List.of("Authorization"));
		config.setAllowCredentials(true);
		config.setMaxAge(3600L);    // cache preflight for 1 hour
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
	
	// ── Authentication Provider ───────────────────────────────────────────────
	@Bean
	public AuthenticationProvider authenticationProvider() {
		
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}
	
	// ── Password Encoder ──────────────────────────────────────────────────────
	@Bean
	public PasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder();
	}
	
	// ── Authentication Manager ────────────────────────────────────────────────
	// Exposed as bean — used in AuthService to authenticate login requests
	@Bean
	public AuthenticationManager authenticationManager(
		AuthenticationConfiguration config) throws Exception {
		
		return config.getAuthenticationManager();
	}
	
}