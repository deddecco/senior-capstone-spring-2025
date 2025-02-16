package edu.uis.csc478.sp25.jobtracker.service;

import edu.uis.csc478.sp25.jobtracker.model.CurrentProfileResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserProfileService {
     public CurrentProfileResponse getCurrentProfile() {
          return CurrentProfileResponse.builder()
                  .id(UUID.fromString("ad16ea53-cd57-4f95-95fa-121c35df1f91"))
                  .name("Alice")
                  .email("alice@alice.com")
                  .currentTitle("SDE I")
                  .bio("this is alice's bio")
                  .location("georgia, usa")
                  .build();
     }

     /*public void setTimeZone(TimeZoneRequest timeZone) {
          System.out.println("Setting time zone to " + timeZone.getTimeZone());
     }*/
}
