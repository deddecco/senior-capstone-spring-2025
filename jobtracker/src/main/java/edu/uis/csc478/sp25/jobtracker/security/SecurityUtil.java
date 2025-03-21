package edu.uis.csc478.sp25.jobtracker.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

public class SecurityUtil {

     /*
      * @return UUID of the logged-in user
      * @throws RuntimeException if no valid authentication or `user_id` claim is found
      */
     public static UUID getLoggedInUserId() {
          Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

          if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
               throw new RuntimeException("No valid JWT authentication found");
          }

          String userId = jwt.getClaim("sub");

          if (userId == null) {
               throw new RuntimeException("No user ID found in JWT claims");
          }

          try {
               return UUID.fromString(userId);
          } catch (IllegalArgumentException e) {
               throw new RuntimeException("User ID in JWT is not a valid UUID", e);
          }
     }

}