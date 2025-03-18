package edu.uis.csc478.sp25.jobtracker.controller;

import edu.uis.csc478.sp25.jobtracker.model.Profile;
import edu.uis.csc478.sp25.jobtracker.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

import static java.util.Map.of;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.internalServerError;
import static org.springframework.http.ResponseEntity.ok;

@CrossOrigin
@RestController
@RequestMapping("/profiles")
public class ProfileController {

     private final ProfileService service;

     public ProfileController(ProfileService service) {
          this.service = service;
     }

     // Get Current Profile
     @GetMapping("/current")
     public ResponseEntity<Profile> getCurrentProfile() {
          return service.getCurrentProfile();
     }

     /**
      * @param profile a profile to be updated
      * @return a ResponseEntity with the success or failure of the update
      */
     @PutMapping("/current")
     public ResponseEntity<String> updateCurrentProfile(@RequestBody Profile profile) {
          try {
               service.updateCurrentProfile(profile);
               return ok("Profile updated successfully!");
          } catch (Exception e) {
               return internalServerError().body("Failed to update profile: " + e.getMessage());
          }
     }

     // Get Profile By ID (Admin or Owner)
     @GetMapping("/{profileId}")
     @PreAuthorize("hasRole('ADMIN') or @profileSecurity.checkOwner(#profileId)")
     public ResponseEntity<Object> getProfileById(@PathVariable UUID profileId) {
          ResponseEntity<Profile> profileResponse = service.getProfileById(profileId);
          if (profileResponse.getBody() == null) {
               Map<String, Object> errorResponse = of(
                       "status", NOT_FOUND.value(),
                       "error", "Not Found",
                       "message", "A profile with ID " + profileId + " does not exist"
               );
               return new ResponseEntity<>(errorResponse, NOT_FOUND);
          }
          return ok(profileResponse.getBody());
     }

     /**
      * @param profileId the id of a profile to be updated
      * @param profile   the new details
      * @return a ResponseEntity with the success or failure of the update operation
      */
     @PutMapping("/{profileId}")
     @PreAuthorize("hasRole('ADMIN') or @profileSecurity.checkOwner(#profileId)")
     public ResponseEntity<String> updateProfileById(@PathVariable UUID profileId,
                                                     @RequestBody Profile profile) {
          try {
               return service.updateProfileById(profileId, profile);
          } catch (Exception e) {
               return internalServerError().body("Failed to update profile: " + e.getMessage());
          }
     }

     /**
      * @param profile a new profile
      * @return a ResponseEntity indicating success or failure of the create operation
      */
     @PostMapping
     public ResponseEntity<String> createProfile(@RequestBody Profile profile) {
          try {
               return service.createProfile(profile);
          } catch (Exception e) {
               return internalServerError().body("Failed to create profile: " + e.getMessage());
          }
     }
}
