package edu.uis.csc478.sp25.jobtracker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.List;

import static org.springframework.security.oauth2.jwt.NimbusJwtDecoder.withSecretKey;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

     @Value("${supabase.jwt.secret}")
     private String jwtSecret;

     @Bean
     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
          http
                  .cors(cors -> cors.configurationSource(corsConfigurationSource())) //CORS setup
                  .csrf(csrf -> csrf.disable()) //Disable CSRF (only for JWT auth, NOT for sessions)
                  .authorizeHttpRequests(auth -> auth
                          .requestMatchers("/public/**").permitAll() // Public endpoints
                          .anyRequest().authenticated() // Secure everything else
                  )
                  .oauth2ResourceServer(oauth2 -> oauth2
                          .jwt(jwt -> jwt.decoder(jwtDecoder()))
                  );

          return http.build();
     }

     @Bean
     public JwtDecoder jwtDecoder() {
          SecretKey key = new SecretKeySpec(jwtSecret.getBytes(), "HmacSHA256");
          return withSecretKey(key).build();
     }

     @Bean
     public CorsConfigurationSource corsConfigurationSource() {
          CorsConfiguration config = new CorsConfiguration();
          config.setAllowedOrigins(List.of("http://localhost:3000"));
          config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
          config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
          config.setAllowCredentials(true);

          UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
          source.registerCorsConfiguration("/**", config);

          return source;
     }
}
