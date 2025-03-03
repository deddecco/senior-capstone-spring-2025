package edu.uis.csc478.sp25.jobtracker.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserIdExtractor {

     public UUID getLoggedInUserId() {
          Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
          if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
               String userId = jwt.getClaim("user_id");
               if (userId != null) {
                    return UUID.fromString(userId);
               }
          }
          throw new RuntimeException("No valid authentication found");
     }
}