package com.example.demo.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.demo.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
   
   @Autowired
   private JwtAuthenticationFilter jwtAuthenticationFilter;
   
   @Bean
   protected DefaultSecurityFilterChain securityFilterChain(
         HttpSecurity http) throws Exception {

      http
         .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
         .csrf(csrfConfigurer -> csrfConfigurer.disable())
         .httpBasic(httpBasicConfigurer -> httpBasicConfigurer.disable())
         .sessionManagement(sessionManagementConfigurer ->
               sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
           )
         
         .authorizeHttpRequests(authorizeRequestsConfigurer -> 
            authorizeRequestsConfigurer
            .requestMatchers("/", "/auth/**").permitAll()
            .anyRequest().authenticated()
         );

      http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

      return http.build();
   }

   @Bean
   public CorsConfigurationSource corsConfigurationSource() {
      CorsConfiguration configuration = new CorsConfiguration();
      configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // 프론트엔드 주소
      configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
      configuration.setAllowedHeaders(Arrays.asList("*"));
      configuration.setAllowCredentials(true);
      
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      source.registerCorsConfiguration("/**", configuration);
      return source;
   }
}