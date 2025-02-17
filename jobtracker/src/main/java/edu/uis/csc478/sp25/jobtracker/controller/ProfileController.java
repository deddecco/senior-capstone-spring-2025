package edu.uis.csc478.sp25.jobtracker.controller;

import edu.uis.csc478.sp25.jobtracker.model.Profile;
import edu.uis.csc478.sp25.jobtracker.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.*;
import static org.springframework.http.ResponseEntity.internalServerError;

@RestController
@RequestMapping("/profile")
// this class calls the service
public class ProfileController {
     private final ProfileService service;

     public ProfileController(ProfileService service) {
          this.service = service;
     }

     // get requests
     @GetMapping("/current")
     public ResponseEntity<Profile> getCurrentProfile() {
          return service.getCurrentProfile();
     }

     // post requests
     @PostMapping("/save")
     public ResponseEntity<String> saveProfile(@RequestBody Profile profile) {
          try {
               service.saveProfile(profile);
               return ok("Profile saved successfully!");
          } catch (Exception e) {
               return internalServerError().body("Failed to save profile: " + e.getMessage());
          }
     }
}
