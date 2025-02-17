package edu.uis.csc478.sp25.jobtracker.service;

import edu.uis.csc478.sp25.jobtracker.model.Profile;
import edu.uis.csc478.sp25.jobtracker.repository.ProfileRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static java.util.UUID.fromString;
import static org.springframework.http.HttpStatus.*;

@Service
public class ProfileService {
     private final ProfileRepository repository;

     public ProfileService(ProfileRepository repository) {
          this.repository = repository;
     }

     // currently logged in
     // Get the profile of the currently logged-in user
     public ResponseEntity<Profile> getCurrentProfile() {
          UUID id = fromString("02ba21b1-4432-4267-a5ca-639774679244"); // Placeholder for current user ID
          Optional<Profile> profile = repository.findById(id);

          if (profile.isPresent()) {
               return new ResponseEntity<>(profile.get(), OK);
          } else {
               return new ResponseEntity<>(NOT_FOUND);
          }
     }

     // Update the profile of the currently logged-in user
     public ResponseEntity<String> updateCurrentProfile(Profile updatedProfile) {
          try {
               UUID id = fromString("02ba21b1-4432-4267-a5ca-639774679244"); // Placeholder for current user ID
               Optional<Profile> existingProfile = repository.findById(id);
               return updateProfile(updatedProfile, existingProfile);
          } catch (Exception e) {
               return new ResponseEntity<>("Failed to update profile: " + e.getMessage(), INTERNAL_SERVER_ERROR);
          }
     }

     private ResponseEntity<String> updateProfile(Profile updatedProfile, Optional<Profile> existingProfile) {
          if (existingProfile.isPresent()) {
               Profile profileToUpdate = existingProfile.get();
               profileToUpdate.setName(updatedProfile.getName()); // Example: update name
               profileToUpdate.setEmail(updatedProfile.getEmail()); // Example: update email
               repository.save(profileToUpdate);
               return new ResponseEntity<>("Profile updated successfully.", OK);
          } else {
               return new ResponseEntity<>("Profile not found.", NOT_FOUND);
          }
     }


     // admin-only
     // Get a specific profile by ID
     public ResponseEntity<Profile> getProfileById(UUID profileId) {
          Optional<Profile> profile = repository.findById(fromString(profileId.toString()));

          if (profile.isPresent()) {
               return new ResponseEntity<>(profile.get(), OK);
          } else {
               return new ResponseEntity<>(NOT_FOUND);
          }
     }

     // admin-only
     // Update a specific profile by ID
     public ResponseEntity<String> updateProfileById(UUID profileId, Profile updatedProfile) {
          try {
               Optional<Profile> existingProfile = repository.findById(fromString(profileId.toString()));
               return updateProfile(updatedProfile, existingProfile);
          } catch (Exception e) {
               return new ResponseEntity<>("Failed to update profile: " + e.getMessage(),
                       INTERNAL_SERVER_ERROR);
          }
     }

     // admin-only
     // Create a new profile (Admin only)
     public ResponseEntity<String> createProfile(Profile newProfile) {
          try {
               repository.save(newProfile);
               return new ResponseEntity<>("Profile created successfully.", CREATED);
          } catch (Exception e) {
               return new ResponseEntity<>("Failed to create profile: " + e.getMessage(), INTERNAL_SERVER_ERROR);
          }
     }
}