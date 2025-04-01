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

import static java.util.Map.*;
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

     /**
      * Get the profile of the currently logged-in user.
      * @return ResponseEntity containing the profile or an error message.
      */
     @GetMapping("/current")
     public ResponseEntity<Object> getCurrentProfile() {
          try {
               Profile profile = service.getCurrentProfile();
               return ok(profile);
          } catch (RuntimeException e) {
               logger.error("Error getting current profile", e);
               if (e.getMessage().contains("not found")) {
                    return status(NOT_FOUND).body(of("message", "Profile not found"));
               }
               return status(INTERNAL_SERVER_ERROR).body(of("message",
                       "An error occurred while fetching the profile"));
          }
     }

     /**
      * Update the profile of the currently logged-in user.
      * @param profile Profile object containing updated values.
      * @return ResponseEntity indicating success or failure.
      */
     @PutMapping("/current")
     public ResponseEntity<Object> updateCurrentProfile(@RequestBody Profile profile) {
          try {
               Profile updatedProfile = service.updateCurrentProfile(profile);
               return ok(updatedProfile);
          } catch (RuntimeException e) {
               logger.error("Failed to update current profile", e);
               if (e.getMessage().contains("not found")) {
                    return status(NOT_FOUND).body(of("message", "Profile not found"));
               }
               return status(INTERNAL_SERVER_ERROR).body(of("message",
                       "An error occurred while updating the profile"));
          }
     }

     /**
      * @param profileId UUID of the profile to fetch.
      * @return ResponseEntity containing the profile or an error message.
      */
     // fixme--- check if asking for own profile, else not let in
     @GetMapping("/{profileId}")
     public ResponseEntity<Object> getProfileById(@PathVariable UUID profileId) {
          try {
               UUID loggedInUserId = SecurityUtil.getLoggedInUserId();
               if (!profileId.equals(loggedInUserId)) {
                    return status(FORBIDDEN).body(of("message",
                            "You do not have permission to access this profile"));
               }

               Profile profile = service.getProfileById(profileId);
               return ok(profile);
          } catch (IllegalStateException e) {
               logger.error("Error getting logged-in user ID", e);
               return status(INTERNAL_SERVER_ERROR).body(of("message", "An error occurred while authenticating the user"));
          } catch (RuntimeException e) {
               logger.error("Error fetching profile by ID {}", profileId, e);
               if (e.getMessage().contains("not found")) {
                    return status(NOT_FOUND).body(of("message",
                            "A profile with ID " + profileId +
                                    " does not exist"));
               }
               return status(INTERNAL_SERVER_ERROR).body(of("message",
                       "An error occurred while fetching the profile"));
          }
     }

     /**
      * Update a specific profile by ID (Admin).
      * @param profileId UUID of the profile to update.
      * @param profile   Profile object containing updated values.
      * @return ResponseEntity containing the updated profile or an error message.
      */
     @PutMapping("/{profileId}")
     public ResponseEntity<Object> updateProfileById(@PathVariable UUID profileId,
                                                     @RequestBody Profile profile) {
          try {
               Profile updatedProfile = service.updateProfileById(profileId, profile);
               return ok(updatedProfile);
          } catch (RuntimeException e) {
               logger.error("Failed to update profile with ID {}", profileId, e);
               if (e.getMessage().contains("not found")) {
                    return status(NOT_FOUND).body(of("message", "Profile not found"));
               }
               return status(INTERNAL_SERVER_ERROR).body(of("message",
                       "An error occurred while updating the profile"));
          }
     }

     /**
      * Create a new profile for the logged-in user.
      * @param profile Profile object containing new values.
      * @return ResponseEntity indicating success or failure.
      */
     @PostMapping
     public ResponseEntity<Object> createProfile(@RequestBody Profile profile) {
          try {
               UUID userId = getLoggedInUserId();
               Profile createdProfile = service.createProfile(userId, profile);
               return status(CREATED).body(createdProfile);
          } catch (RuntimeException e) {
               logger.error("Failed to create profile", e);
               if (e.getMessage().contains("already exists")) {
                    return status(CONFLICT).body(of("message",
                            "A profile already exists for this user"));
               }
               return status(INTERNAL_SERVER_ERROR).body(of("message",
                       "An error occurred while creating the profile"));
          }
     }

     /**
      * Helper method to get the logged-in user's ID from SecurityUtil.
      * @return UUID of the logged-in user.
      */
     private UUID getLoggedInUserId() {
          try {
               return SecurityUtil.getLoggedInUserId();
          } catch (Exception e) {
               logger.error("Error getting logged-in user ID", e);
               throw new IllegalStateException("Unable to get logged-in user ID", e);
          }
     }
}