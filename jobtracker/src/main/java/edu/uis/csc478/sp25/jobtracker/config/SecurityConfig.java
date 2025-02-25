package edu.uis.csc478.sp25.jobtracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

     @Bean
     public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtDecoder jwtDecoder) throws Exception {
          http
                  .authorizeHttpRequests(authorize -> authorize
                          .anyRequest().authenticated()
                  )
                  .oauth2ResourceServer(oauth2 -> oauth2
                          .jwt(jwt -> jwt.decoder(jwtDecoder))
                  );
          return http.build();
     }
}
