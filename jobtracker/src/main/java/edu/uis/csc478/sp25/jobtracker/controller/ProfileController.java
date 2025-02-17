package edu.uis.csc478.sp25.jobtracker.controller;

import edu.uis.csc478.sp25.jobtracker.model.Profile;
import edu.uis.csc478.sp25.jobtracker.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.ResponseEntity.internalServerError;
import static org.springframework.http.ResponseEntity.ok;

@RestController
// *** THIS IS NOW "PROFILES", NO LONGER "PROFILE" *** //
@RequestMapping("/profiles")
public class ProfileController {
     private final ProfileService service;

     public ProfileController(ProfileService service) {
          this.service = service;
     }

     ///////////
     // USER //
     /////////

     // PASS
     // current user only
     // GET /profiles/current
     // get your own profile
     @GetMapping("/current")
     public ResponseEntity<Profile> getCurrentProfile() {
          return service.getCurrentProfile();
     }

     // PASS
     // current user
     // PUT /profiles/current
     // update your own profile
     @PutMapping("/current")
     public ResponseEntity<String> updateCurrentProfile(@RequestBody Profile profile) {
          try {
               service.updateCurrentProfile(profile);
               return ok("Profile updated successfully!");
          } catch (Exception e) {
               return internalServerError().body("Failed to update profile: " + e.getMessage());
          }
     }

     ////////////
     // ADMIN //
    ///////////

     // PASS
     // admin only
     // GET /profiles/{profileId}
     // see a profile as an admin; send a GET with an ID
     @GetMapping("/{profileId}")
     public ResponseEntity<Profile> getProfileById(@PathVariable UUID profileId) {
          return service.getProfileById(profileId);
     }

     // PASS
     // admin only
     // PUT /profiles/{profileId}
     // update a profile as an admin; send a PUT with an ID
     @PutMapping("/{profileId}")
     public ResponseEntity<String> updateProfileById(@PathVariable UUID profileId,
                                                     @RequestBody Profile profile) {
          try {
               service.updateProfileById(profileId, profile);
               return ok("Profile updated successfully!");
          } catch (Exception e) {
               return internalServerError().body("Failed to update profile: " + e.getMessage());
          }
     }
     // PASS
     // admin only
     // POST /profiles
     // create a profile as an admin; send a POST without an ID
     @PostMapping
     public ResponseEntity<String> createProfile(@RequestBody Profile profile) {
          try {
               service.createProfile(profile);
               return ok("Profile created successfully!");
          } catch (Exception e) {
               return internalServerError().body("Failed to create profile: " + e.getMessage());
          }
     }
}