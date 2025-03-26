package edu.uis.csc478.sp25.jobtracker.service;

import edu.uis.csc478.sp25.jobtracker.model.Profile;
import edu.uis.csc478.sp25.jobtracker.repository.ProfileRepository;
import edu.uis.csc478.sp25.jobtracker.security.SecurityUtil;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.beans.BeanUtils.copyProperties;

@Service
public class ProfileService {

     private final ProfileRepository repository;

     public ProfileService(ProfileRepository repository) {
          this.repository = repository;
     }

     // Helper method to get the logged-in user's ID
     private UUID getLoggedInUserId() {
          return SecurityUtil.getLoggedInUserId();
     }

     /*
      * Get the profile of the currently logged-in user
      * @return the profile entity
      * @throws RuntimeException if the profile is not found
      */
     public Profile getCurrentProfile() {
          UUID loggedInUserId = getLoggedInUserId();
          Optional<Profile> profile = repository.findById(loggedInUserId);

          if (profile.isPresent()) {
               return profile.get();
          } else {
               throw new RuntimeException("Profile not found.");
          }
     }

     /*
      * Update the profile of the currently logged-in user
      * @param updatedProfile the profile with new values to be applied
      * @return the updated Profile entity
      * @throws RuntimeException if the profile is not found
      */
     public Profile updateCurrentProfile(Profile updatedProfile) {
          UUID loggedInUserId = getLoggedInUserId();
          Optional<Profile> existingProfileOptional = repository.findById(loggedInUserId);

          if (existingProfileOptional.isPresent()) {
               Profile existingProfile = existingProfileOptional.get();
               return applyProfileUpdates(existingProfile, updatedProfile);
          } else {
               throw new RuntimeException("Profile not found.");
          }
     }

     /*
      * Get a specific profile by ID (Admin)
      * @param profileId the ID of the profile to retrieve
      * @return the profile entity
      * @throws RuntimeException if the profile is not found
      */
     public Profile getProfileById(UUID profileId) {
          Optional<Profile> profile = repository.findById(profileId);

          if (profile.isPresent()) {
               return profile.get();
          } else {
               throw new RuntimeException("Profile not found.");
          }
     }

     /*
      * Update a specific profile by ID (Admin)
      * @param profileId      the ID of the profile to update
      * @param updatedProfile the profile with new values to be applied
      * @return the updated Profile entity
      * @throws RuntimeException if the profile is not found
      */
     public Profile updateProfileById(UUID profileId, Profile updatedProfile) {
          Optional<Profile> existingProfileOptional = repository.findById(profileId);

          if (existingProfileOptional.isPresent()) {
               Profile existingProfile = existingProfileOptional.get();
               return applyProfileUpdates(existingProfile, updatedProfile);
          } else {
               throw new RuntimeException("Profile not found.");
          }
     }

     /*
      * Create a new profile for a user
      * @param userId  the ID of the user to associate with this profile
      * @param profile the profile data to create
      * @return the created Profile entity
      * @throws RuntimeException if a profile already exists for this user
      */
     public Profile createProfile(UUID userId, Profile profile) {
          // Check if a profile already exists for this user ID
          if (repository.existsById(userId)) {
               throw new RuntimeException("Profile already exists for this user.");
          }

          // Set the user ID in the profile object
          profile.setId(userId);

          // Use custom insert method to save the new profile in database
          repository.insertProfile(
                  profile.getId(),
                  profile.getName(),
                  profile.getEmail(),
                  profile.getTitle(),
                  profile.getBio(),
                  profile.getLocation()
          );

          // Return the created Profile object
          return profile;
     }
     /**
      * Utility method to copy properties from an updated profile to an existing one.
      *
      * @param existingProfile The current persisted entity from DB.
      * @param updatedProfile  The new data to be applied.
      * @return The saved and updated entity.
      */
     private Profile applyProfileUpdates(Profile existingProfile, Profile updatedProfile) {
          // Exclude 'id' to prevent overwriting it accidentally.
          copyProperties(updatedProfile, existingProfile, "id");
          return repository.save(existingProfile);
     }
}