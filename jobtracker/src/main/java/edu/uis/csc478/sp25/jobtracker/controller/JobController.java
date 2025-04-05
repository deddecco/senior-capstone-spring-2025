package edu.uis.csc478.sp25.jobtracker.controller;

import edu.uis.csc478.sp25.jobtracker.model.Job;
import edu.uis.csc478.sp25.jobtracker.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static edu.uis.csc478.sp25.jobtracker.security.SecurityUtil.getLoggedInUserId;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.*;

@CrossOrigin
@RestController
@RequestMapping("/jobs")
public class JobController {

     private static final Logger logger = LoggerFactory.getLogger(JobController.class);
     private final JobService service;

     public JobController(JobService service) {
          this.service = service;
     }

     @GetMapping
     public ResponseEntity<List<Job>> getAllJobs() {
          try {
               List<Job> userJobs = service.getJobsForCurrentUser();
               return userJobs.isEmpty() ? noContent().build() : ok(userJobs);
          } catch (Exception e) {
               logger.error("Error fetching jobs for user", e);
               return status(INTERNAL_SERVER_ERROR).build();
          }
     }

     @GetMapping("/{id}")
     public ResponseEntity<Object> getJobById(@PathVariable UUID id) {
          try {
               Job job = service.getJobById(id);
               return ok(job);
          } catch (RuntimeException e) {
               logger.error("Error fetching job by ID {}", id, e);
               if (e.getMessage().contains("not found")) {
                    return status(NOT_FOUND)
                            .body(of("message", "Job not found or you don't have permission to view it"));
               }
               return status(INTERNAL_SERVER_ERROR)
                       .body(of("message", "An error occurred while fetching the job"));
          }
     }

     // Search jobs based on optional filters
     @GetMapping({"/search", "/search/"})
     public ResponseEntity<List<Job>> searchJobs(
             @RequestParam(required = false) String title,
             @RequestParam(required = false) String level,
             @RequestParam(required = false) Integer minSalary,
             @RequestParam(required = false) Integer maxSalary,
             @RequestParam(required = false) String location,
             @RequestParam(required = false) String status) {
          try {
               logger.info("Received search request with filters: title={}, level={}, minSalary={}, maxSalary={}, location={}, status={}",
                       title, level, minSalary, maxSalary, location, status);

               // Normalize input parameters
               String normalizedTitle = (title != null) ? title.trim() : null;
               String normalizedLevel = (level != null) ? level.trim() : null;
               String normalizedLocation = (location != null) ? location.trim() : null;
               String normalizedStatus = (status != null) ? status.trim() : null;

               // Fetch matching jobs from the service layer
               List<Job> matchingJobs = service.searchJobs(normalizedTitle, normalizedLevel, minSalary, maxSalary, normalizedLocation, normalizedStatus);

               // Return appropriate response based on results
               return matchingJobs.isEmpty()
                       ? noContent().build()
                       : ok(matchingJobs);
          } catch (Exception e) {
               logger.error("Error searching jobs", e);
               return status(INTERNAL_SERVER_ERROR).build();
          }
     }

     @GetMapping("/status-counts")
     public ResponseEntity<Map<String, Integer>> getJobStatusCounts() {
          try {
               // Fetch status counts from the service layer
               Map<String, Integer> statusCounts = service.getJobStatusCounts();

               // If statusCounts is empty, return 204 No Content
               if (statusCounts == null || statusCounts.isEmpty()) {
                    logger.warn("No job status counts found for the current user");
                    return noContent().build();
               }

               // Return the status counts
               return ok(statusCounts);
          } catch (RuntimeException e) {
               logger.error("Error fetching job status counts", e);
               return status(INTERNAL_SERVER_ERROR)
                       .body(of()); // Return an empty map on error
          }
     }


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

               // Handle specific conflict case
               if (e.getMessage().contains("already exists")) {
                    return status(CONFLICT).body(of("message",
                            "Job with these details already exists for this user"));
               }

               return status(INTERNAL_SERVER_ERROR)
                       .body(of("message", "An error occurred while creating the job"));
          }
     }

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

     @DeleteMapping("/{id}")
     public ResponseEntity<Object> deleteJob(@PathVariable UUID id) {
          try {
               service.deleteJob(id);
               return ok(of("message", "Job deleted successfully"));
          } catch (RuntimeException e) {
               logger.error("Failed to delete job with ID {}", id, e);
               if (e.getMessage().contains("not found")) {
                    return status(NOT_FOUND).body(of("message",
                            e.getMessage()));
               }
               return status(INTERNAL_SERVER_ERROR).body(of("message",
                       "Failed to delete job"));
          }
     }
}