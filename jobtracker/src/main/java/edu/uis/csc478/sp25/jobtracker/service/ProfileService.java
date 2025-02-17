package edu.uis.csc478.sp25.jobtracker.service;

import edu.uis.csc478.sp25.jobtracker.model.Profile;
import edu.uis.csc478.sp25.jobtracker.repository.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProfileService {
     private final ProfileRepository repository;

     public ProfileService(ProfileRepository repository) {
          this.repository = repository;
     }

     public Profile getCurrentProfile() {
          UUID id = UUID.fromString("02ba21b1-4432-4267-a5ca-639774679244");
          return repository.findById(id).orElseThrow();
     }

     public void saveProfile(Profile toBeSaved){
          System.out.println("saving profile...");
          repository.save(toBeSaved);
     }

}
