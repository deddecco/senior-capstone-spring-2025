package edu.uis.csc478.sp25.jobtracker.security;

import edu.uis.csc478.sp25.jobtracker.model.Profile;
import edu.uis.csc478.sp25.jobtracker.service.ProfileService;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("profileSecurity")
public class ProfileSecurityExpressionHandler {

     private final ProfileService profileService;

     public ProfileSecurityExpressionHandler(ProfileService profileService) {
          this.profileService = profileService;
     }

     public boolean checkOwner(UUID id) {
          Profile profile = profileService.getProfileById(id).getBody();
          if (profile != null) {
               UUID userId = SecurityUtil.getLoggedInUserId();
               return profile.user_id.equals(userId);
          }
          return false;
     }
}
