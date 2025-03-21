package edu.uis.csc478.sp25.jobtracker.service;

import edu.uis.csc478.sp25.jobtracker.model.Job;
import edu.uis.csc478.sp25.jobtracker.repository.JobRepository;
import edu.uis.csc478.sp25.jobtracker.security.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.springframework.beans.BeanUtils.copyProperties;
import static org.springframework.http.HttpStatus.*;

@Service
public class JobService {

     private static final Logger logger = LoggerFactory.getLogger(JobService.class);
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

     // Get a specific job by ID
     public ResponseEntity<Job> getJobById(UUID jobId) {
          try {
               Optional<Job> job = repository.findById(jobId);
               return job.map(ResponseEntity::ok)
                       .orElse(new ResponseEntity<>(NOT_FOUND));
          } catch (DataAccessException e) {
               logger.error("Error fetching job by ID", e);
               return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
          }
     }

     // Create a new job
     public ResponseEntity<String> createJob(Job newJob) {
          try {
               if (!isValidJob(newJob)) {
                    return new ResponseEntity<>("Invalid job data", BAD_REQUEST);
               }
               UUID userId = getLoggedInUserId();

               newJob.setUserId(userId);
               repository.save(newJob);
               return new ResponseEntity<>("Job created successfully.", CREATED);
          } catch (DataAccessException e) {
               logger.error("Failed to create job due to a database error", e);
               return new ResponseEntity<>("Failed to create job due to a database error.", INTERNAL_SERVER_ERROR);
          } catch (Exception e) {
               logger.error("Unexpected error while creating job", e);
               return new ResponseEntity<>("An unexpected error occurred.", INTERNAL_SERVER_ERROR);
          }
     }

     // Update a specific job by ID
     public ResponseEntity<String> updateJob(UUID jobId, Job updatedJob) {
          try {
               UUID userId = getLoggedInUserId();
               Optional<Job> existingJob = repository.findByIdAndUserId(jobId, userId);
               return existingJob.map(job -> applyJobUpdates(job, updatedJob)).orElseGet(() -> new ResponseEntity<>("Job not found or you don't have permission to update it.", NOT_FOUND));
          } catch (DataAccessException e) {
               logger.error("Failed to update job with ID {}", jobId, e);
               return new ResponseEntity<>("Failed to update job due to a database error.", INTERNAL_SERVER_ERROR);
          } catch (Exception e) {
               logger.error("Unexpected error while updating job with ID {}", jobId, e);
               return new ResponseEntity<>("An unexpected error occurred.", INTERNAL_SERVER_ERROR);
          }
     }

     private ResponseEntity<String> applyJobUpdates(Job existingJob, Job updatedJob) {
          try {
               copyProperties(updatedJob, existingJob, "id", "userId");
               repository.save(existingJob);
               return new ResponseEntity<>("Job updated successfully.", OK);
          } catch (BeansException e) {
               logger.error("Error applying job updates", e);
               return new ResponseEntity<>("Failed to update job properties.", INTERNAL_SERVER_ERROR);
          } catch (DataAccessException e) {
               logger.error("Error saving updated job", e);
               return new ResponseEntity<>("Failed to save updated job.", INTERNAL_SERVER_ERROR);
          }
     }

     // Delete a job by ID
     public ResponseEntity<String> deleteJob(UUID jobId) {
          try {
               UUID userId = getLoggedInUserId();
               if (!repository.existsByIdAndUserId(jobId, userId)) {
                    return new ResponseEntity<>("Job not found or you don't have permission to delete it.", NOT_FOUND);
               }
               repository.deleteById(jobId);
               return new ResponseEntity<>("Job deleted successfully.", OK);
          } catch (DataAccessException e) {
               logger.error("Failed to delete job with ID {}", jobId, e);
               return new ResponseEntity<>("Failed to delete job due to a database error.", INTERNAL_SERVER_ERROR);
          } catch (Exception e) {
               logger.error("Unexpected error while deleting job with ID {}", jobId, e);
               return new ResponseEntity<>("An unexpected error occurred.", INTERNAL_SERVER_ERROR);
          }
     }

     // Search jobs
     public List<Job> searchJobs(String title, String level, Integer minSalary, Integer maxSalary, String location) {
          try {
               UUID userId = getLoggedInUserId();
               return repository.findByFilters(userId, title, level, minSalary, maxSalary, location);
          } catch (DataAccessException e) {
               logger.error("Error searching jobs", e);
               return Collections.emptyList();
          }
     }

     // Count jobs by status
     public Map<String, Integer> getJobStatusCounts() {
          try {
               UUID userId = getLoggedInUserId();
               List<Object[]> results = repository.countJobsByStatus(userId);
               Map<String, Integer> statusCounts = new HashMap<>();

               String[] statuses = {"Saved", "Applied", "Screening", "Interview", "Rejected", "Offer", "Hired"};
               for (String status : statuses) {
                    statusCounts.put(status, 0);
               }

               for (Object[] result : results) {
                    String status = (String) result[0];
                    Integer count = ((Number) result[1]).intValue();
                    statusCounts.put(status, count);
               }

               return statusCounts;
          } catch (DataAccessException e) {
               logger.error("Error fetching job status counts", e);
               return Collections.emptyMap();
          }
     }

     private boolean isValidJob(Job job) {
          return job != null &&
                  job.getTitle() != null && !job.getTitle().trim().isEmpty() &&
                  job.getLevel() != null && !job.getLevel().trim().isEmpty() &&
                  job.getStatus() != null && !job.getStatus().trim().isEmpty() &&
                  job.getMinSalary() >= 0 &&
                  job.getMaxSalary() >= job.getMinSalary() &&
                  job.getLocation() != null && !job.getLocation().trim().isEmpty();
     }
}
