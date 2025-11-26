package com.hamzabnr.ClientModelCrud.Config;

import com.hamzabnr.ClientModelCrud.Security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

  // BCrypt encoder bean
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * SecurityFilterChain explicitly receives the UserDetailsService bean so that
   * Spring Security will use your DB-backed service for authentication.
   *
   * Note: ensure you DON'T define any other UserDetailsService beans elsewhere.
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .cors(Customizer.withDefaults())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/users/login").permitAll()
            .requestMatchers(HttpMethod.GET, "/clients/**").hasAnyRole("USER", "ADMIN")
            .requestMatchers(HttpMethod.POST, "/clients").hasRole("ADMIN")
            .requestMatchers(HttpMethod.POST, "/clients/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/clients/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/clients/**").hasRole("ADMIN")
            .anyRequest().authenticated()
        )
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
