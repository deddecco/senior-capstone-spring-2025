package edu.uis.csc478.sp25.jobtracker.service;

import edu.uis.csc478.sp25.jobtracker.model.Profile;
import edu.uis.csc478.sp25.jobtracker.repository.ProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static java.util.UUID.*;

@Service
public class ProfileService {
     private final ProfileRepository repository;

     public ProfileService(ProfileRepository repository) {
          this.repository = repository;
     }

     public ResponseEntity<Profile> getCurrentProfile() {
          UUID id = fromString("02ba21b1-4432-4267-a5ca-639774679244");
          // optional-- i.e., if there is a profile, return it, and if not, return null
          Optional<Profile> profile = repository.findById(id);

          // if we have a profile
          if (profile.isPresent()) {
               // return it, plus the status code 200
               return new ResponseEntity<>(profile.get(), HttpStatus.OK);
          } else {
               // otherwise return the status code 404
               return new ResponseEntity<>(HttpStatus.NOT_FOUND);
          }
     }

     public ResponseEntity<String> saveProfile(Profile toBeSaved) {
          try {
               // try to save a profile
               repository.save(toBeSaved);
               // if it works, return 200
               return new ResponseEntity<>("Profile saved successfully.", HttpStatus.OK);
          } catch (Exception e) {
               // if it doesn't work, return 500
               return new ResponseEntity<>("Failed to save profile: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
          }
     }
}
