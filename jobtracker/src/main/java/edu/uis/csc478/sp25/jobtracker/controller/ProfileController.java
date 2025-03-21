package edu.uis.csc478.sp25.jobtracker.controller;

import edu.uis.csc478.sp25.jobtracker.model.Profile;
import edu.uis.csc478.sp25.jobtracker.security.SecurityUtil;
import edu.uis.csc478.sp25.jobtracker.service.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@CrossOrigin
@RestController
@RequestMapping("/profiles")
public class ProfileController {

     private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);
     private final ProfileService service;

     public ProfileController(ProfileService service) {
          this.service = service;
     }

     // Get Current Profile
     @GetMapping("/current")
     public ResponseEntity<Profile> getCurrentProfile() {
          try {
               return service.getCurrentProfile();
          } catch (Exception e) {
               logger.error("Error getting current profile", e);
               return status(INTERNAL_SERVER_ERROR).build();
          }
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
               logger.error("Failed to update current profile", e);
               return status(INTERNAL_SERVER_ERROR).body("Failed to update profile");
          }
     }

     // Get Profile By ID (Admin)
     @GetMapping("/{profileId}")
     public ResponseEntity<Object> getProfileById(@PathVariable UUID profileId) {
          ResponseEntity<Profile> profileResponse = service.getProfileById(profileId);
          if (profileResponse.getBody() != null) {
               return ok(profileResponse.getBody());
          }
          Map<String, Object> errorResponse = Map.of(
                  "status", NOT_FOUND.value(),
                  "error", "Not Found",
                  "message", "A profile with ID " + profileId + " does not exist"
          );
          return status(NOT_FOUND).body(errorResponse);
     }

     /**
      * @param profileId the id of a profile to be updated
      * @param profile   the new details
      * @return a ResponseEntity with the success or failure of the update operation
      */
     @PutMapping("/{profileId}")
     public ResponseEntity<String> updateProfileById(@PathVariable UUID profileId,
                                                     @RequestBody Profile profile) {
          try {
               return service.updateProfileById(profileId, profile);
          } catch (Exception e) {
               logger.error("Failed to update profile with ID {}", profileId, e);
               return status(INTERNAL_SERVER_ERROR).body("Failed to update profile");
          }
     }

     /**
      * @param profile a new profile
      * @return a ResponseEntity indicating success or failure of the create operation
      */
     @PostMapping
     public ResponseEntity createProfile(@RequestBody Profile profile) {
          try {
               Profile created = service.createProfile(getLoggedInUserId(), profile);
               return new ResponseEntity<>(OK);
          } catch (Exception e) {
               logger.error("Failed to create profile", e);
               return status(INTERNAL_SERVER_ERROR).body("Failed to create profile");
          }
     }

     private UUID getLoggedInUserId() {
          try {
               return SecurityUtil.getLoggedInUserId();
          } catch (Exception e) {
               logger.error("Error getting logged-in user ID", e);
               throw new IllegalStateException("Unable to get logged-in user ID", e);
          }
     }
}