package edu.uis.csc478.sp25.jobtracker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import static org.springframework.security.oauth2.jwt.NimbusJwtDecoder.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

     @Value("${supabase.jwt.secret}")
     private String jwtSecret;

     @Bean
     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
          http
                  .authorizeHttpRequests(authorize -> authorize
                          .anyRequest().authenticated()
                  )
                  .oauth2ResourceServer(oauth2 -> oauth2
                          .jwt(jwt -> jwt.decoder(jwtDecoder()))
                  );
          return http.build();
     }

     @Bean
     public JwtDecoder jwtDecoder() {
          // Use the JWT secret from Supabase
          SecretKey key = new SecretKeySpec(jwtSecret.getBytes(), "HmacSHA256");
          return withSecretKey(key).build();
     }
}