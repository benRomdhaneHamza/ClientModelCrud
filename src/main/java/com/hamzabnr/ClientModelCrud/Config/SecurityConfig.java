package com.hamzabnr.ClientModelCrud.Config;

import com.hamzabnr.ClientModelCrud.Exceptions.AccessUnauthorizedException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

@Configuration
public class SecurityConfig {

  @Bean
  public UserDetailsService userDetailsService() {
    UserDetails user = User.withUsername("hamza")
        .password("{noop}user")
        .roles("USER")
        .build();
    UserDetails admin = User.withUsername("admin")
        .password("{noop}admin")
        .roles("ADMIN")
        .build();
    return new InMemoryUserDetailsManager(user, admin);
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/v3/api-docs/**"
            ).permitAll()

            // ROLE restrictions
            .requestMatchers(HttpMethod.GET, "/clients/**").hasAnyRole("USER", "ADMIN")
            .requestMatchers(HttpMethod.POST, "/clients/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/clients/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/clients/**").hasRole("ADMIN")

            .anyRequest().authenticated()
        )
        .httpBasic(Customizer.withDefaults());

    return http.build();
  }

//  @Bean
//  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//    http
//        .csrf(csrf -> csrf.disable())
//        .authorizeHttpRequests(auth -> auth
//            // Swagger open
//            .requestMatchers(
//                "/swagger-ui.html",
//                "/swagger-ui/**",
//                "/v3/api-docs/**"
//            ).permitAll()
//
//            // POST /clients requires authentication
//            .requestMatchers(HttpMethod.POST, "/clients").hasRole("ADMIN").anyRequest().authenticated()
//
//            // GET /clients stays public
//            .requestMatchers(HttpMethod.GET, "/clients").permitAll()
//
//            // /clients/{id} requires authentication
//            .requestMatchers(new RegexRequestMatcher("^/clients/[^/]+$", null))
//            .authenticated()
//
//            // Everything else
//            .anyRequest().permitAll()
//        )
//        .httpBasic(Customizer.withDefaults());
//
//    return http.build();
//  }

}
