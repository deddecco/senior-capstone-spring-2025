package edu.uis.csc478.sp25.jobtracker.controller;

import edu.uis.csc478.sp25.jobtracker.model.Profile;
import edu.uis.csc478.sp25.jobtracker.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.util.UUID.*;
import static org.springframework.http.HttpStatus.*;
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

     ///////////
     // USER //

     /// //////

     // Get Current Profile
     @GetMapping("/current")
     public ResponseEntity<Profile> getCurrentProfile() {
          UUID loggedInId = getLoggedInUserId();

          ResponseEntity<Profile> profileResponse = service.getCurrentProfile(loggedInId);
          if (profileResponse.getBody() == null) {
               return new ResponseEntity<>(NOT_FOUND);
          }
          return ok(profileResponse.getBody());
     }


     // Update Current Profile
     @PutMapping("/current")
     public ResponseEntity<String> updateCurrentProfile(@RequestBody Profile profile) {
          UUID loggedInId = getLoggedInUserId();

          try {
               service.updateCurrentProfile(loggedInId, profile);
               return ok("Profile updated successfully!");
          } catch (Exception e) {
               return internalServerError().body("Failed to update profile: " + e.getMessage());
          }
     }


     ////////////
     // ADMIN //

     /// ////////

// Get Profile By ID (Admin)
     @GetMapping("/{profileId}")
     public ResponseEntity<Object> getProfileById(@PathVariable UUID profileId) {
          ResponseEntity<Profile> profileResponse = service.getProfileById(profileId);
          if (profileResponse.getBody() == null) {
               Map<String, Object> errorResponse = new HashMap<>();
               errorResponse.put("status", HttpStatus.NOT_FOUND.value());
               errorResponse.put("error", "Not Found");
               errorResponse.put("message", "A profile with ID " + profileId + " does not exist");

               return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
          }
          return ok(profileResponse.getBody());
     }


     // Update Profile By ID (Admin)
     @PutMapping("/{profileId}")
     public ResponseEntity<String> updateProfileById(@PathVariable UUID profileId,
                                                     @RequestBody Profile profile) {
          try {
               if (!service.existsById(profileId)) {
                    return new ResponseEntity<>("Profile not found", NOT_FOUND);
               }
               service.updateProfileById(profileId, profile);
               return ok("Profile updated successfully!");
          } catch (Exception e) {
               return internalServerError().body("Failed to update profile: " + e.getMessage());
          }
     }

     // Create New Profile (Admin)
     @PostMapping
     public ResponseEntity<String> createProfile(@RequestBody Profile profile) {
          try {
               // Check if the email already exists
               if (!service.existsByEmail(profile.getEmail())) {
                    // Use CREATED status for creation
                    service.createProfile(profile);
                    return new ResponseEntity<>("Profile created successfully!", CREATED);
               } else {
                    return new ResponseEntity<>("Email already exists.", CONFLICT);
               }

          } catch (Exception e) {
               return internalServerError().body("Failed to create profile: " + e.getMessage());
          }
     }

     private UUID getLoggedInUserId() {
          Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
          if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
               String userId = jwt.getClaim("user_id");
               if (userId != null) {
                    return fromString(userId);
               }
          }
          throw new RuntimeException("No valid authentication found");
     }

}