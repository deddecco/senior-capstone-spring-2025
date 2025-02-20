package edu.uis.csc478.sp25.jobtracker.service;

import edu.uis.csc478.sp25.jobtracker.model.Profile;
import edu.uis.csc478.sp25.jobtracker.repository.ProfileRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.beans.BeanUtils.copyProperties;
import static org.springframework.http.HttpStatus.*;

@Service
public class ProfileService {
     private final ProfileRepository repository;

     public ProfileService(ProfileRepository repository) {
          this.repository = repository;
     }

     // Get the profile of the currently logged-in user
     public ResponseEntity<Profile> getCurrentProfile() {
          // Placeholder for current user ID
          UUID id = UUID.fromString("02ba21b1-4432-4267-a5ca-639774679244");
          Optional<Profile> profile = repository.findById(id);

          if (profile.isPresent()) {
               return new ResponseEntity<>(profile.get(), OK);
          } else {
               return new ResponseEntity<>(NOT_FOUND);
          }
     }

     // Update the profile of the currently logged-in user
     public ResponseEntity<String> updateCurrentProfile(Profile updatedProfile) {
          // Placeholder for current user ID
          UUID id = UUID.fromString("02ba21b1-4432-4267-a5ca-639774679244");
          Optional<Profile> existingProfile = repository.findById(id);

          if (existingProfile.isPresent()) {
               return applyProfileUpdates(existingProfile.get(), updatedProfile);
          } else {
               return new ResponseEntity<>("Profile not found.", NOT_FOUND);
          }
     }

     // Get a specific profile by ID (Admin)
     public ResponseEntity<Profile> getProfileById(UUID profileId) {
          Optional<Profile> profile = repository.findById(profileId);

          if (profile.isPresent()) {
               return new ResponseEntity<>(profile.get(), OK);
          } else {
               return new ResponseEntity<>(NOT_FOUND);
          }
     }

     // Update a specific profile by ID (Admin)
     public ResponseEntity<String> updateProfileById(UUID profileId, Profile updatedProfile) {
          Optional<Profile> existingProfile = repository.findById(profileId);

          if (existingProfile.isPresent()) {
               return applyProfileUpdates(existingProfile.get(), updatedProfile);
          } else {
               return new ResponseEntity<>("Profile not found.", NOT_FOUND);
          }
     }

     // Create a new profile (Admin only)
     public ResponseEntity<String> createProfile(Profile newProfile) {
          if (existsByEmail(newProfile.getEmail())) {
               return new ResponseEntity<>("Email already exists.", CONFLICT);
          }
          repository.save(newProfile);
          return new ResponseEntity<>("Profile created successfully.", CREATED);
     }

     // Check if a profile exists by ID
     public boolean existsById(UUID profileId) {
          return repository.existsById(profileId);
     }

     // Check if a profile with the same email exists
     public boolean existsByEmail(String email) {
          return repository.existsByEmail(email);
     }

     // Utility method to copy properties from the updated profile to the existing one
     private ResponseEntity<String> applyProfileUpdates(Profile existingProfile, Profile updatedProfile) {
          copyProperties(updatedProfile, existingProfile, "id"); // Exclude 'id' to prevent overwriting
          repository.save(existingProfile);
          return new ResponseEntity<>("Profile updated successfully.", OK);
     }
}