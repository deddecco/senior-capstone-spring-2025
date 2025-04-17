package edu.uis.csc478.sp25.jobtracker.service;

import edu.uis.csc478.sp25.jobtracker.model.Job;
import edu.uis.csc478.sp25.jobtracker.repository.JobRepository;
import edu.uis.csc478.sp25.jobtracker.security.SecurityUtil;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Collections.emptyMap;
import static java.util.UUID.randomUUID;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.beans.BeanUtils.copyProperties;

/**
 * Service layer for managing Job entities.
 * All operations are scoped to the currently logged-in user for security and data isolation.
 * Handles business logic for job CRUD, search, status aggregation, and favorite management.
 */
@Service
public class JobService {

     private static final Logger logger = getLogger(JobService.class);
     private final JobRepository repository;

     /**
      * Constructs a JobService with the given JobRepository.
      * @param repository the JobRepository used for data access
      */
     public JobService(JobRepository repository) {
          this.repository = repository;
     }

     /**
      * Helper method to get the currently logged-in user's UUID.
      * Uses SecurityUtil to retrieve the user context.
      * @return UUID of the authenticated user
      * @throws IllegalStateException if the user ID cannot be determined
      */
     private UUID getLoggedInUserId() {
          try {
               return SecurityUtil.getLoggedInUserId();
          } catch (Exception e) {
               logger.error("Error getting logged-in user ID", e);
               throw new IllegalStateException("Unable to get logged-in user ID", e);
          }
     }

     /**
      * Retrieves all jobs for the currently logged-in user, sorted by last_modified descending.
      * @return List of Job objects (may be empty)
      * @throws DataAccessException if a database error occurs
      */
     public List<Job> getJobsForCurrentUser() {
          try {
               UUID userId = getLoggedInUserId();
               // Query repository for all jobs belonging to the user, ordered by last_modified
               return repository.findAllByUserIdOrderByLastModifiedDesc(userId);
          } catch (DataAccessException e) {
               logger.error("Error fetching jobs for user", e);
               throw e;
          }
     }

     /**
      * Retrieves a specific job by ID for the current user.
      * Enforces that the job must belong to the authenticated user.
      * @param jobId the UUID of the job to retrieve
      * @return the Job object if found
      * @throws RuntimeException if not found or not owned by the user, or on DB error
      */
     public Job getJobById(UUID jobId) {
          try {
               UUID userId = getLoggedInUserId();
               Optional<Job> jobOptional = repository.findByIdAndUserId(jobId, userId);

               // If present, return the job; otherwise, throw an error for not found/unauthorized
               if (jobOptional.isPresent()) {
                    return jobOptional.get();
               } else {
                    throw new RuntimeException("Job not found or you don't have permission to access it.");
               }
          } catch (DataAccessException e) {
               logger.error("Error fetching job by ID", e);
               throw new RuntimeException("Error accessing database", e);
          }
     }

     /**
      * Creates a new job for the current user.
      * Automatically assigns the current user as the job owner and generates a UUID if missing.
      * @param newJob the Job object to create
      * @return the created Job object
      * @throws IllegalArgumentException if the job data is invalid
      * @throws RuntimeException on database or unexpected errors
      */
     public Job createJob(Job newJob) {
          try {
               // Validate job fields before proceeding
               if (!isValidJob(newJob)) {
                    throw new IllegalArgumentException("Invalid job data");
               }

               UUID userId = getLoggedInUserId();
               newJob.setUserId(userId);

               // If no ID is set, generate a new UUID
               if (newJob.getId() == null) {
                    newJob.setId(randomUUID());
               }

               // Insert the job using a custom repository method (for full control)
               repository.insertJob(newJob);

               // Fetch the job back from the DB to verify creation and return it
               Optional<Job> optionalJob = repository.findById(newJob.getId());
               if (optionalJob.isPresent()) {
                    return optionalJob.get();
               } else {
                    throw new RuntimeException("Job not found after creation");
               }
          } catch (DataAccessException e) {
               logger.error("Failed to create job due to a database error", e);
               throw new RuntimeException("Failed to create job due to a database error", e);
          } catch (IllegalArgumentException e) {
               logger.warn("Invalid job data provided", e);
               throw e;
          } catch (Exception e) {
               logger.error("Unexpected error while creating job", e);
               throw new RuntimeException("An unexpected error occurred", e);
          }
     }

     /**
      * Updates a specific job by ID for the current user.
      * Only allows update if the job exists and is owned by the user.
      * @param jobId      the UUID of the job to update
      * @param updatedJob the Job object with updated values
      * @return the updated Job object
      * @throws IllegalArgumentException if the updated job data is invalid
      * @throws RuntimeException if the job is not found, not owned, or on DB error
      */
     public Job updateJob(UUID jobId, Job updatedJob) {
          try {
               UUID userId = getLoggedInUserId();
               Optional<Job> jobOptional = repository.findByIdAndUserId(jobId, userId);
               Job existingJob;

               // Ensure the job exists and is owned by the user
               if (jobOptional.isPresent()) {
                    existingJob = jobOptional.get();
               } else {
                    throw new RuntimeException("Job not found or you don't have permission to update it.");
               }

               // Copy updatable fields from updatedJob to existingJob (excluding id/userId)
               Job updatedJobWithChanges = applyJobUpdates(existingJob, updatedJob);

               // Validate the updated job before saving
               if (!isValidJob(updatedJobWithChanges)) {
                    throw new IllegalArgumentException("Invalid job data");
               }

               return updatedJobWithChanges;
          } catch (DataAccessException e) {
               logger.error("Failed to update job with ID {}", jobId, e);
               throw new RuntimeException("Failed to update job due to a database error", e);
          } catch (IllegalArgumentException e) {
               logger.warn("Invalid job data provided for update", e);
               throw e;
          } catch (RuntimeException e) {
               logger.warn("Error updating job with ID {}: {}", jobId, e.getMessage());
               throw e;
          } catch (Exception e) {
               logger.error("Unexpected error while updating job with ID {}", jobId, e);
               throw new RuntimeException("An unexpected error occurred", e);
          }
     }

     /**
      * Applies updates from the updated job to the existing job, excluding id and userId.
      * Uses Spring's BeanUtils to copy properties.
      * @param existingJob the job from the database
      * @param updatedJob  the job with new values
      * @return the updated job after saving
      * @throws RuntimeException if there's an error applying updates or saving
      */
     private Job applyJobUpdates(Job existingJob, Job updatedJob) {
          // Exclude 'id' and 'userId' to prevent overwriting them accidentally.
          copyProperties(updatedJob, existingJob, "id", "userId");
          return repository.save(existingJob);
     }

     /**
      * Deletes a job by ID for the current user.
      * Only deletes if the job exists and is owned by the user.
      * @param jobId the UUID of the job to delete
      * @throws RuntimeException if the job is not found, not owned, or on DB error
      */
     public void deleteJob(UUID jobId) {
          try {
               UUID userId = getLoggedInUserId();
               // Check for existence and ownership before deletion
               if (!repository.existsByIdAndUserId(jobId, userId)) {
                    throw new RuntimeException("Job not found or you don't have permission to delete it.");
               }
               repository.deleteById(jobId);
          } catch (DataAccessException e) {
               logger.error("Failed to delete job with ID {}", jobId, e);
               throw new RuntimeException("Failed to delete job due to a database error", e);
          } catch (RuntimeException e) {
               throw e;
          } catch (Exception e) {
               logger.error("Unexpected error while deleting job with ID {}", jobId, e);
               throw new RuntimeException("An unexpected error occurred", e);
          }
     }

     /**
      * Searches jobs for the current user based on optional filters.
      * Any parameter may be null, in which case it is ignored in the search.
      * @param title     optional job title filter
      * @param level     optional job level filter
      * @param minSalary optional minimum salary filter
      * @param maxSalary optional maximum salary filter
      * @param location  optional job location filter
      * @param status    optional job status filter
      * @param company   optional company filter
      * @param favorite  optional favorite filter (true/false, or null for any)
      * @return List of matching Job objects (may be empty)
      * @throws RuntimeException if a database or unexpected error occurs
      */
     public List<Job> searchJobs(
             String title,
             String level,
             Integer minSalary,
             Integer maxSalary,
             String location,
             String status,
             String company,
             Boolean favorite
     ) {
          try {
               UUID userId = getLoggedInUserId();
               // Delegate to repository, which supports filtering on any combination of parameters
               return repository.findJobsByFilters(
                       userId,
                       title,
                       level,
                       minSalary,
                       maxSalary,
                       location,
                       status,
                       company,
                       favorite
               );
          } catch (Exception e) {
               logger.error("Error searching jobs for user", e);
               throw new RuntimeException("Failed to search jobs", e);
          }
     }

     /**
      * Retrieves a map of job statuses and their counts for the logged-in user.
      * Returns an empty map if the user has no jobs.
      * @return map of status to count
      * @throws RuntimeException if a database or unexpected error occurs
      */
     public Map<String, Integer> getJobStatusCounts() {
          try {
               // Get the logged-in user's ID
               UUID userId = getLoggedInUserId();
               logger.info("Fetching job status counts for user ID: {}", userId);

               // Fetch all jobs associated with the user
               List<Job> allUserJobs = repository.findByUserId(userId);
               logger.info("User has {} total jobs", allUserJobs.size());

               // If no jobs exist, return an empty map
               if (allUserJobs.isEmpty()) {
                    return emptyMap();
               }

               // Initialize a map with default statuses and counts set to 0
               Map<String, Integer> statusCounts = new HashMap<>();
               String[] statuses = {"Saved", "Applied", "Screening", "Interview", "Rejected", "Offer", "Hired"};
               for (String status : statuses) {
                    statusCounts.put(status, 0);
               }

               // Count jobs by their statuses
               for (Job job : allUserJobs) {
                    String status = job.getStatus();
                    if (status != null && !status.isEmpty()) {
                         statusCounts.put(status, statusCounts.getOrDefault(status, 0) + 1);
                    }
               }

               logger.info("Job status counts: {}", statusCounts);
               return statusCounts;
          } catch (Exception e) {
               // Log the error and rethrow it as a runtime exception
               logger.error("Error fetching job status counts", e);
               throw new RuntimeException("Failed to get job status counts", e);
          }
     }

     /**
      * Validates whether a job object has all required fields filled in and logical values.
      * Used before creating or updating jobs.
      * @param job the Job object to validate
      * @return true if the job is valid, false otherwise
      */
     private boolean isValidJob(Job job) {
          return job != null &&
                  job.getTitle() != null && !job.getTitle().trim().isEmpty() &&
                  job.getLevel() != null && !job.getLevel().trim().isEmpty() &&
                  job.getStatus() != null && !job.getStatus().trim().isEmpty() &&
                  job.getMinSalary() >= 0 &&
                  job.getMaxSalary() >= job.getMinSalary() &&
                  job.getLocation() != null && !job.getLocation().trim().isEmpty();
     }

     /**
      * Marks a job as favorite for the current user.
      * Only updates if the job is not already favorited.
      * @param jobId the UUID of the job to favorite
      * @return the updated Job object
      * @throws RuntimeException if the job is not found or not owned
      */
     public Job favoriteJob(UUID jobId) {
          Job job = getJobById(jobId);
          // Only update if not already favorite to avoid unnecessary DB writes
          if (!job.isFavorite()) {
               job.setFavorite(true);
               return repository.save(job);
          }
          // Already favorite, return as is
          return job;
     }

     /**
      * Retrieves all favorite jobs for the current user.
      * @return List of favorite Job objects (may be empty)
      */
     public List<Job> getFavoriteJobs() {
          UUID userId = getLoggedInUserId();
          return repository.findByUserIdAndFavoriteTrue(userId);
     }

     /**
      * Unmarks a job as favorite for the current user.
      * Only updates if the job is currently favorited.
      * @param jobId the UUID of the job to unfavorite
      * @return the updated Job object
      * @throws RuntimeException if the job is not found or not owned
      */
     public Job unfavoriteJob(UUID jobId) {
          Job job = getJobById(jobId);
          // Only update if currently favorite to avoid unnecessary DB writes
          if (job.isFavorite()) {
               job.setFavorite(false);
               return repository.save(job);
          }
          // Already not favorite, return as is
          return job;
     }
}