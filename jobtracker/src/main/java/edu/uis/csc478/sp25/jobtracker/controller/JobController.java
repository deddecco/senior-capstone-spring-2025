package edu.uis.csc478.sp25.jobtracker.controller;

import edu.uis.csc478.sp25.jobtracker.model.Job;
import edu.uis.csc478.sp25.jobtracker.service.JobService;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static edu.uis.csc478.sp25.jobtracker.security.SecurityUtil.getLoggedInUserId;
import static java.util.Map.of;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.*;

/**
 * REST controller for managing Job resources.
 * Handles CRUD operations, search, and favorite/unfavorite actions for jobs.
 */
@CrossOrigin
@RestController
@RequestMapping("/jobs")
public class JobController {

     private static final Logger logger = getLogger(JobController.class);
     private final JobService service;

     /**
      * Constructs a new JobController with the given JobService.
      * @param service the JobService used for business logic
      */
     public JobController(JobService service) {
          this.service = service;
     }

     /**
      * Retrieves all jobs for the currently authenticated user.
      * @return 200 OK with the list of jobs, or 204 No Content if none found
      */
     @GetMapping
     public ResponseEntity<List<Job>> getAllJobs() {
          try {
               List<Job> userJobs = service.getJobsForCurrentUser();
               // If the list is empty, return 204 No Content;
               // else, return 200 OK with the list.
               return userJobs.isEmpty() ? noContent().build() : ok(userJobs);
          } catch (Exception e) {
               logger.error("Error fetching jobs for user", e);
               return status(INTERNAL_SERVER_ERROR).build();
          }
     }

     /**
      * Retrieves a specific job by its ID for the current user.
      * @param id the UUID of the job
      * @return 200 OK with the job, 404 if not found, or 500 on error
      */
     @GetMapping("/{id}")
     public ResponseEntity<Object> getJobById(@PathVariable UUID id) {
          try {
               Job job = service.getJobById(id);
               return ok(job);
          } catch (RuntimeException e) {
               logger.error("Error fetching job by ID {}", id, e);
               // If the exception message contains "not found", return 404 Not Found; otherwise, return 500 Internal Server Error.
               if (e.getMessage().contains("not found")) {
                    return status(NOT_FOUND)
                            .body(of("message", "Job not found or you don't have permission to view it"));
               }
               return status(INTERNAL_SERVER_ERROR)
                       .body(of("message", "An error occurred while fetching the job"));
          }
     }

     /**
      * Searches jobs for the current user based on optional filters.
      *
      * @param title     optional job title filter
      * @param level     optional job level filter
      * @param minSalary optional minimum salary filter
      * @param maxSalary optional maximum salary filter
      * @param location  optional job location filter
      * @param status    optional job status filter
      * @param company   optional company filter
      * @param favorite  optional favorite filter (true/false)
      * @return 200 OK with matching jobs, 204 No Content if none, or 500 on error
      */
     @GetMapping({"/search", "/search/"})
     public ResponseEntity<List<Job>> searchJobs(
             @RequestParam(required = false) String title,
             @RequestParam(required = false) String level,
             @RequestParam(required = false) Integer minSalary,
             @RequestParam(required = false) Integer maxSalary,
             @RequestParam(required = false) String location,
             @RequestParam(required = false) String status,
             @RequestParam(required = false) String company,
             @RequestParam(required = false) Boolean favorite
     ) {
          try {
               logger.info("Received search request with filters: title={}, " +
                               "level={}, " +
                               "minSalary={}, " +
                               "maxSalary={}, " +
                               "location={}, " +
                               "status={}, " +
                               "company={}, " +
                               "favorite={}",
                       title,
                       level,
                       minSalary,
                       maxSalary,
                       location,
                       status,
                       company,
                       favorite);

               // Ternary operator: If the parameter is not null, trim it; else, leave it null.
               String normalizedTitle = (title != null) ? title.trim() : null;
               String normalizedLevel = (level != null) ? level.trim() : null;
               String normalizedLocation = (location != null) ? location.trim() : null;
               String normalizedStatus = (status != null) ? status.trim() : null;
               String normalizedCompany = (company != null) ? company.trim() : null;

               // Fetch matching jobs from the service layer
               List<Job> matchingJobs = service.searchJobs(
                       normalizedTitle,
                       normalizedLevel,
                       minSalary,
                       maxSalary,
                       normalizedLocation,
                       normalizedStatus,
                       normalizedCompany,
                       favorite
               );

               // If no jobs match, return 204 No Content;
               // else, return 200 OK with jobs.
               return matchingJobs.isEmpty() ? noContent().build() : ok(matchingJobs);
          } catch (Exception e) {
               logger.error("Error searching jobs", e);
               return status(INTERNAL_SERVER_ERROR).build();
          }
     }

     /**
      * Retrieves job status counts for the current user.
      * @return 200 OK with a map of status to count, 204 if none, or 500 on error
      */
     @GetMapping("/status-counts")
     public ResponseEntity<Map<String, Integer>> getJobStatusCounts() {
          try {
               // Fetch status counts from the service layer
               Map<String, Integer> statusCounts = service.getJobStatusCounts();

               // If statusCounts is empty, return 204 No Content; else, return 200 OK.
               if (statusCounts == null || statusCounts.isEmpty()) {
                    logger.warn("No job status counts found for the current user");
                    return noContent().build();
               }

               // Return the status counts
               return ok(statusCounts);
          } catch (RuntimeException e) {
               logger.error("Error fetching job status counts", e);
               return status(INTERNAL_SERVER_ERROR).body(of());
          }
     }

     /**
      * Creates a new job for the current user.
      * @param job the job to create
      * @return 201 Created with the new job, 409 if duplicate, or 500 on error
      */
     @PostMapping
     public ResponseEntity<Object> createJob(@RequestBody Job job) {
          try {
               // Get current user
               UUID userId = getLoggedInUserId();
               // Ensure job is tied to current user
               job.setUserId(userId);
               Job createdJob = service.createJob(job);
               return status(CREATED).body(createdJob);
          } catch (RuntimeException e) {
               logger.error("Failed to create job", e);

               // If the exception message contains "already exists", return 409 Conflict; else, 500 Internal Server Error.
               if (e.getMessage().contains("already exists")) {
                    return status(CONFLICT).body(of("message",
                            "Job with these details already exists for this user"));
               }

               return status(INTERNAL_SERVER_ERROR)
                       .body(of("message", "An error occurred while creating the job"));
          }
     }

     /**
      * Updates an existing job for the current user.
      * @param id  the UUID of the job to update
      * @param job the job data to update
      * @return 200 OK with the updated job, 400 if invalid, or 500 on error
      */
     @PutMapping("/{id}")
     public ResponseEntity<Object> updateJob(@PathVariable UUID id, @RequestBody Job job) {
          try {
               Job updatedJob = service.updateJob(id, job);
               return ok(updatedJob);
          } catch (IllegalArgumentException e) {
               logger.warn("Invalid job data for update: {}", e.getMessage());
               return badRequest().body(of("message", e.getMessage()));
          } catch (RuntimeException e) {
               logger.error("Error updating job with ID {}", id, e);
               return status(INTERNAL_SERVER_ERROR)
                       .body(of("message", "An unexpected error occurred while updating the job"));
          }
     }

     /**
      * Deletes a job for the current user.
      * @param id the UUID of the job to delete
      * @return 200 OK if deleted, 404 if not found, or 500 on error
      */
     @DeleteMapping("/{id}")
     public ResponseEntity<Object> deleteJob(@PathVariable UUID id) {
          try {
               service.deleteJob(id);
               return ok(of("message", "Job deleted successfully"));
          } catch (RuntimeException e) {
               logger.error("Failed to delete job with ID {}", id, e);
               // If the exception message contains "not found", return 404 Not Found; else, 500 Internal Server Error.
               if (e.getMessage().contains("not found")) {
                    return status(NOT_FOUND).body(of("message", e.getMessage()));
               }
               return status(INTERNAL_SERVER_ERROR).body(of("message", "Failed to delete job"));
          }
     }

     /**
      * Marks a job as favorite for the current user.
      * @param id the UUID of the job to favorite
      * @return 200 OK with the updated job, or 500 on error
      */
     @PutMapping("/{id}/favorite")
     public ResponseEntity<Job> favoriteJob(@PathVariable UUID id) {
          try {
               Job favoritedJob = service.favoriteJob(id);
               return ok(favoritedJob);
          } catch (RuntimeException e) {
               logger.error("Error favoriting job {}", id, e);
               return status(INTERNAL_SERVER_ERROR).build();
          }
     }

     /**
      * Retrieves all favorite jobs for the current user.
      * @return 200 OK with the list of favorite jobs, or 204 if none found
      */
     @GetMapping("/favorites")
     public ResponseEntity<List<Job>> getFavoriteJobs() {
          try {
               List<Job> favoriteJobs = service.getFavoriteJobs();
               // If the list is empty, return 204 No Content;
               // else, return 200 OK with the list.
               return favoriteJobs.isEmpty() ? noContent().build() : ok(favoriteJobs);
          } catch (Exception e) {
               logger.error("Error fetching favorite jobs", e);
               return status(INTERNAL_SERVER_ERROR).build();
          }
     }

     /**
      * Removes a job from favorites for the current user.
      * @param id the UUID of the job to unfavorite
      * @return 200 OK with the updated job, or 500 on error
      */
     @PutMapping("/{id}/unfavorite")
     public ResponseEntity<Job> unfavoriteJob(@PathVariable UUID id) {
          try {
               Job unfavoritedJob = service.unfavoriteJob(id);
               return ok(unfavoritedJob);
          } catch (RuntimeException e) {
               logger.error("Error unfavoriting job {}", id, e);
               return status(INTERNAL_SERVER_ERROR).build();
          }
     }
}