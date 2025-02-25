package edu.uis.csc478.sp25.jobtracker.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@Configuration
public class JwtConfig {

     @Bean
     public JwtDecoder jwtDecoder() throws Exception {
          KeyPair keyPair = generateRsaKey();
          RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
          RSAKey rsaKey = new RSAKey.Builder(publicKey)
                  .keyID(UUID.randomUUID().toString())
                  .build();
          JWKSet jwkSet = new JWKSet(rsaKey);
          JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(jwkSet);
          return NimbusJwtDecoder.withPublicKey(publicKey).build();
     }

     private KeyPair generateRsaKey() throws Exception {
          KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
          keyPairGenerator.initialize(2048);
          return keyPairGenerator.generateKeyPair();
     }
}