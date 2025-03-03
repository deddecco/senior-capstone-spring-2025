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

     // controller constructors take in service layers
     public ProfileController(ProfileService service) {
          this.service = service;
     }

     ///////////
     // USER //

     /// //////

     // Get Current Profile
     @GetMapping("/current")
     // if the profile cannot be found, return an error, but if the profile can be found, return it
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
     // take in a profile
     // if the profile attempting to be updated is the profile of the user currently logged in, allow the update
     // otherwise do not, and throw an exception
     public ResponseEntity<String> updateCurrentProfile(@RequestBody Profile profile) {

          // determine the UUID of the current user
          UUID loggedInId = getLoggedInUserId();
          // if they are updating their own, existing profile, let them through
          try {
               service.updateCurrentProfile(loggedInId, profile);
               return ok("Profile updated successfully!");
          }
          // throw an error otherwise
          catch (Exception e) {
               return internalServerError().body("Failed to update profile: " + e.getMessage());
          }
     }


     ////////////
     // ADMIN //

     /// ////////

// Get Profile By ID (Admin)
     // as an admin, see any profile, by its id
     @GetMapping("/{profileId}")
     public ResponseEntity<Object> getProfileById(@PathVariable UUID profileId) {
          ResponseEntity<Profile> profileResponse = service.getProfileById(profileId);
          // if the profile with that id doesn't exist, throw an error
          if (profileResponse.getBody() == null) {
               Map<String, Object> errorResponse = new HashMap<>();
               errorResponse.put("status", HttpStatus.NOT_FOUND.value());
               errorResponse.put("error", "Not Found");
               errorResponse.put("message", "A profile with ID " + profileId + " does not exist");

               return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
          }
          // if it does exist, let an admin user see it
          return ok(profileResponse.getBody());
     }


     // Update Profile By ID (Admin)
     @PutMapping("/{profileId}")
     public ResponseEntity<String> updateProfileById(@PathVariable UUID profileId,
                                                     @RequestBody Profile profile) {
          // if a certain profile exists, allow an admin to update it
          try {
               if (!service.existsById(profileId)) {
                    return new ResponseEntity<>("Profile not found", NOT_FOUND);
               }
               // do the update
               service.updateProfileById(profileId, profile);
               // return an OK status code with this message
               return ok("Profile updated successfully!");
          }
          // if it doesn't exist, throw an error
          catch (Exception e) {
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



     //todo: throw this into another class to remove duplicates

     // determine the UUID of the user currently logged in
     private UUID getLoggedInUserId() {
          // this is from Spring Security
          Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
          // if that object isn't null and is a JWT-- a JSON Web Token
          if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
               // find the user_id (which is a UUID) inside the JWT
               String userId = jwt.getClaim("user_id");
               // if there is an id inside the JWT, return it
               if (userId != null) {
                    return fromString(userId);
               }
          }
          // if there is not, throw an error
          throw new RuntimeException("No valid authentication found");
     }

}