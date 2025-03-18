package edu.uis.csc478.sp25.jobtracker.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SecurityUtil {

     /**
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

     /**
      * @param role the role to check for
      * @return true if the logged-in user has the specified role, false otherwise
      */
     public static boolean hasRole(String role) {
          Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
          if (authentication != null) {
               for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
                    if (grantedAuthority.getAuthority().equals("ROLE_" + role)) {
                         return true;
                    }
               }
               return false;
          }
          return false;
     }

}
