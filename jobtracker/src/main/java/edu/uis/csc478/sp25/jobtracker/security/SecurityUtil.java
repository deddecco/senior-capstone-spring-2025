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
          // get the current authentication object from the security context.
          Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
          // check if authentication exists and if the principal is a JWT.
          if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
               // extract the "user_id" claim from the JWT.
               String userId = jwt.getClaim("user_id");
               // if "user_id" exists, convert it to a UUID and return it.
               if (userId != null) {
                    return UUID.fromString(userId);
               }
          }
          // throw an exception if no valid authentication or "user_id" claim is found.
          throw new RuntimeException("No valid authentication found");
     }
}