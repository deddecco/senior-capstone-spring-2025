package edu.uis.csc478.sp25.jobtracker.service;

import edu.uis.csc478.sp25.jobtracker.model.Profile;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProfileService {
     public Profile getCurrentProfile() {
          return Profile.builder()
                  .id(UUID.fromString("ad16ea53-cd57-4f95-95fa-121c35df1f91"))
                  .name("Alice")
                  .email("alice@alice.com")
                  .title("SDE I")
                  .bio("this is alice's bio")
                  .location("georgia, usa")
                  .build();
     }

     public void saveProfile(Profile toBeSaved){
          System.out.println("saving profile...");

     }

}
