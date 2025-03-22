package edu.uis.csc478.sp25.jobtracker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.List.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.oauth2.jwt.NimbusJwtDecoder.withSecretKey;

@Configuration
@EnableWebSecurity
// DO NOT DELETE THIS FILE
public class SecurityConfig {

     // injects the value of the 'supabase.jwt.secret' property from the environment variables
     // secret is used to validate JWTs issued by Supabase.
     @Value("${supabase.jwt.secret}")
     private String jwtSecret;

     /**
      * configures the security filter chain for the application.
      *
      * @param http HttpSecurity object to configure security settings
      * @return configured SecurityFilterChain
      * @throws Exception if any configuration error occurs
      */
     @Bean
     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
          http
                  .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                  .authorizeHttpRequests(authorize -> authorize
                          // Allow OPTIONS requests without authentication
                          .requestMatchers(OPTIONS, "/**").permitAll()

                          //  all incoming requests must be authenticated
                          .anyRequest().authenticated()
                  )
                  .oauth2ResourceServer(oauth2 -> oauth2
                          // configure JWT-based authentication for the OAuth2 resource server
                          .jwt(jwt -> jwt.decoder(jwtDecoder()))
                  );
          // builds and returns the configured SecurityFilterChain.
          return http.build();
     }

     @Bean
     public CorsConfigurationSource corsConfigurationSource() {
          CorsConfiguration configuration = new CorsConfiguration();
          configuration.setAllowedOrigins(of("*"));
          configuration.setAllowedMethods(asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
          configuration.setAllowedHeaders(asList("authorization", "content-type", "x-auth-token"));
          configuration.setExposedHeaders(of("x-auth-token"));
          // Set to false to allow wildcard origins
          configuration.setAllowCredentials(false);
          UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
          source.registerCorsConfiguration("/**", configuration);
          return source;
     }

     /**
      * creates a JwtDecoder to validate incoming JWTs using the secret key
      * @return a JwtDecoder configured with the secret key
      */
     @Bean
     public JwtDecoder jwtDecoder() {
          // converts the JWT secret string into a SecretKey object using HMAC-SHA256 algorithm.
          SecretKey key = new SecretKeySpec(jwtSecret.getBytes(), "HmacSHA256");
          // creates and returns a NimbusJwtDecoder configured with the secret key for verifying JWT signatures.
          return withSecretKey(key).build();
     }
}