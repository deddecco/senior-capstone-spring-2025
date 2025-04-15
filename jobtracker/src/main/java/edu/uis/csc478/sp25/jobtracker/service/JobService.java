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

@Service
public class JobService {

     private static final Logger logger = getLogger(JobService.class);
     private final JobRepository repository;

     public JobService(JobRepository repository) {
          this.repository = repository;
     }

     // Helper method to get the logged-in user's ID
     private UUID getLoggedInUserId() {
          try {
               return SecurityUtil.getLoggedInUserId();
          } catch (Exception e) {
               logger.error("Error getting logged-in user ID", e);
               throw new IllegalStateException("Unable to get logged-in user ID", e);
          }
     }

     /*
      * @return a list of jobs in the system belonging to the current user
      */
     // Get all jobs for the currently logged-in user
     public List<Job> getJobsForCurrentUser() {
          try {
               UUID userId = getLoggedInUserId();
               return repository.findByUserId(userId);
          } catch (DataAccessException e) {
               logger.error("Error fetching jobs for user", e);
               throw e;
          }
     }

     /**
      * Get a specific job by ID for the current user
      *
      * @param jobId the ID of the job to retrieve
      * @return the job if found
      * @throws RuntimeException if the job is not found or doesn't belong to the user
      */
     public Job getJobById(UUID jobId) {
          try {
               UUID userId = getLoggedInUserId();
               Optional<Job> jobOptional = repository.findByIdAndUserId(jobId, userId);

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
      * Create a new job for the current user
      *
      * @param newJob the job to create
      * @return the created job
      * @throws RuntimeException if the job data is invalid or if there's a database error
      */
     public Job createJob(Job newJob) {
          try {
               if (!isValidJob(newJob)) {
                    throw new IllegalArgumentException("Invalid job data");
               }

               UUID userId = getLoggedInUserId();
               newJob.setUserId(userId);

               // Add null check for ID (Option 2 as requested)
               if (newJob.getId() == null) {
                    newJob.setId(randomUUID());
               }

               repository.insertJob(newJob);
               return newJob;
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

     /*
      * Update a specific job by ID for the current user
      * @param jobId      the ID of the job to update
      * @param updatedJob the job with updated values
      * @return the updated job
      * @throws RuntimeException if the job is not found or if there's a database error
      */
     public Job updateJob(UUID jobId, Job updatedJob) {
          try {
               UUID userId = getLoggedInUserId();
               Optional<Job> jobOptional = repository.findByIdAndUserId(jobId, userId);
               Job existingJob;

               if (jobOptional.isPresent()) {
                    existingJob = jobOptional.get();
               } else {
                    throw new RuntimeException("Job not found or you don't have permission to update it.");
               }

               Job updatedJobWithChanges = applyJobUpdates(existingJob, updatedJob);

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
      * Apply updates from the updated job to the existing job
      *
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
      * Delete a job by ID for the current user
      *
      * @param jobId the ID of the job to delete
      * @throws RuntimeException if the job is not found or if there's a database error
      */
     public void deleteJob(UUID jobId) {
          try {
               UUID userId = getLoggedInUserId();
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
      * @param title     optional parameter
      * @param level     optional parameter
      * @param minSalary optional parameter
      * @param maxSalary optional parameter
      * @param location  optional parameter
      * @param status optional parameter
      * @param company optional parameter
      * @return a list of jobs that match the optional parameters, or all jobs
      */
     // Search jobs
     public List<Job> searchJobs(
             String title,
             String level,
             Integer minSalary,
             Integer maxSalary,
             String location,
             String status,
             String company
     ) {
          try {
               UUID userId = getLoggedInUserId(); // Get logged-in user's ID

               // Delegate to repository with filters, now including company
               return repository.findJobsByFilters(
                       userId,
                       title,
                       level,
                       minSalary,
                       maxSalary,
                       location,
                       status,
                       company
               );
          } catch (Exception e) {
               logger.error("Error searching jobs for user", e);
               throw new RuntimeException("Failed to search jobs", e);
          }
     }

     /**
      * @return key-value pairs of job statuses and how many jobs have each status for the logged-in user
      */
     // this was broken but now works thanks to Nate's fix on 4/4-4/5
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
                         statusCounts.put(status,
                                 statusCounts.getOrDefault(status, 0) + 1);
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
      * @param job a job
      * @return whether a job is valid, i.e., if it has all the required fields filled in or not
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

     public Job saveJobToCollection(UUID jobId) {
          Job job = getJobById(jobId);
          if (!job.getStatus().equals("Saved")) {
               job.setStatus("Saved");
               return repository.save(job);
          }
          // Already saved
          return job;
     }

     public List<Job> getSavedJobs() {
          UUID userId = getLoggedInUserId();
          return repository.findByUserIdAndStatus(userId);
     }

     public Job unsaveJob(UUID jobId) {
          Job job = getJobById(jobId);
          if ("Saved".equals(job.getStatus())) {
               job.setStatus("");
               return repository.save(job);
          }
          return job;
     }
}