package edu.uis.csc478.sp25.jobtracker.service;

import edu.uis.csc478.sp25.jobtracker.model.Profile;
import edu.uis.csc478.sp25.jobtracker.repository.ProfileRepository;
import edu.uis.csc478.sp25.jobtracker.security.SecurityUtil;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.beans.BeanUtils.copyProperties;

/**
 * Service layer for managing Profile entities.
 * Provides business logic for CRUD operations on user profiles.
 * All user-facing operations are scoped to the currently logged-in user for security.
 * Admin operations allow access by profile ID.
 */
@Service
public class ProfileService {

     private final ProfileRepository repository;

     /**
      * Constructs a ProfileService with the given ProfileRepository.
      * @param repository the ProfileRepository used for data access
      */
     public ProfileService(ProfileRepository repository) {
          this.repository = repository;
     }

     /**
      * Helper method to get the currently logged-in user's UUID.
      * Uses SecurityUtil to retrieve the user context.
      * @return UUID of the authenticated user
      */
     private UUID getLoggedInUserId() {
          return SecurityUtil.getLoggedInUserId();
     }

     /**
      * Retrieves the profile of the currently logged-in user.
      * Throws an exception if the profile does not exist.
      * @return the Profile entity for the current user
      * @throws RuntimeException if the profile is not found
      */
     public Profile getCurrentProfile() {
          UUID loggedInUserId = getLoggedInUserId();
          Optional<Profile> profile = repository.findById(loggedInUserId);

          // If the profile exists, return it; otherwise, throw an error.
          if (profile.isPresent()) {
               return profile.get();
          } else {
               throw new RuntimeException("Profile not found.");
          }
     }

     /**
      * Updates the profile of the currently logged-in user.
      * Only allows update if the profile exists.
      * @param updatedProfile the profile with new values to be applied
      * @return the updated Profile entity
      * @throws RuntimeException if the profile is not found
      */
     public Profile updateCurrentProfile(Profile updatedProfile) {
          UUID loggedInUserId = getLoggedInUserId();
          Optional<Profile> existingProfileOptional = repository.findById(loggedInUserId);

          // Only update if the profile exists
          if (existingProfileOptional.isPresent()) {
               Profile existingProfile = existingProfileOptional.get();
               return applyProfileUpdates(existingProfile, updatedProfile);
          } else {
               throw new RuntimeException("Profile not found.");
          }
     }

     /**
      * Retrieves a specific profile by ID.
      * Intended for admin use.
      * @param profileId the ID of the profile to retrieve
      * @return the Profile entity
      * @throws RuntimeException if the profile is not found
      */
     public Profile getProfileById(UUID profileId) {
          Optional<Profile> profile = repository.findById(profileId);

          // If the profile exists, return it; otherwise, throw an error.
          if (profile.isPresent()) {
               return profile.get();
          } else {
               throw new RuntimeException("Profile not found.");
          }
     }

     /**
      * Updates a specific profile by ID.
      * Intended for admin use. Only allows update if the profile exists.
      * @param profileId      the ID of the profile to update
      * @param updatedProfile the profile with new values to be applied
      * @return the updated Profile entity
      * @throws RuntimeException if the profile is not found
      */
     public Profile updateProfileById(UUID profileId, Profile updatedProfile) {
          Optional<Profile> existingProfileOptional = repository.findById(profileId);

          // Only update if the profile exists
          if (existingProfileOptional.isPresent()) {
               Profile existingProfile = existingProfileOptional.get();
               return applyProfileUpdates(existingProfile, updatedProfile);
          } else {
               throw new RuntimeException("Profile not found.");
          }
     }

     /**
      * Creates a new profile for a user.
      * Fails if a profile already exists for the user.
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

          // Set the user ID in the profile object (profile PK == user PK)
          profile.setId(userId);

          // Use custom insert method to save the new profile in the database
          repository.insertProfile(
                  profile.getId(),
                  profile.getName(),
                  profile.getEmail(),
                  profile.getTitle(),
                  profile.getBio(),
                  profile.getLocation(),
                  profile.getPhoneNumber()
          );

          // Return the created Profile object (may not have DB-generated fields if any)
          return profile;
     }

     /**
      * Utility method to copy properties from an updated profile to an existing one.
      * Excludes 'id' to prevent overwriting the primary key.
      * @param existingProfile The current persisted entity from DB.
      * @param updatedProfile  The new data to be applied.
      * @return The saved and updated entity.
      */
     private Profile applyProfileUpdates(Profile existingProfile, Profile updatedProfile) {
          // Exclude 'id' to prevent overwriting it accidentally.
          copyProperties(updatedProfile, existingProfile, "id");
          // Save the updated profile to the repository
          return repository.save(existingProfile);
     }
}