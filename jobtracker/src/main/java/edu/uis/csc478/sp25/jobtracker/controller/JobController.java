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


     // todo test
     @GetMapping("/search")
     public ResponseEntity<List<Job>> searchJobs(
             @RequestParam(required = false) String title,
             @RequestParam(required = false) String level,
             @RequestParam(required = false) Integer minSalary,
             @RequestParam(required = false) Integer maxSalary,
             @RequestParam(required = false) String location) {
          try {
               List<Job> matchingJobs = service.searchJobs(title, level, minSalary, maxSalary, location);
               return matchingJobs.isEmpty() ? noContent().build() : ok(matchingJobs);
          } catch (Exception e) {
               logger.error("Error searching jobs", e);
               return status(INTERNAL_SERVER_ERROR).build();
          }
     }

     // fixme: shows OK response but with nothing in list when SpongeBob has jobs
     @GetMapping("/status-counts")
     public ResponseEntity<Map<String, Integer>> getJobStatusCounts() {
          try {
               Map<String, Integer> statusCounts = service.getJobStatusCounts();
               return ResponseEntity.ok(statusCounts);
          } catch (RuntimeException e) {
               logger.error("Error fetching job status counts", e);
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(of());
          }
     }

     @PostMapping
     public ResponseEntity<Object> createJob(@RequestBody Job job) {
          if (!isValidJob(job)) {
               return badRequest().body(of("message", "Invalid job data"));
          }
          try {
               Job createdJob = service.createJob(job);
               return status(CREATED).body(createdJob);
          } catch (RuntimeException e) {
               logger.error("Failed to create job", e);
               return status(INTERNAL_SERVER_ERROR)
                       .body(of("message", "An error occurred while creating the job"));
          }
     }

     @PutMapping("/{id}")
     public ResponseEntity<Object> updateJob(@PathVariable UUID id, @RequestBody Job job) {
          // Validate job object
          if (job == null) {
               logger.error("Job is null");
               return badRequest().body(of("message", "Job data is missing"));
          }

          // Validate job ID
          if (job.getId() == null) {
               logger.warn("Job ID is null, setting it from path variable");
               job.setId(id); // Automatically set ID from path variable
          } else if (!job.getId().equals(id)) {
               logger.error("Job ID mismatch: Path ID = {}, Job ID = {}", id, job.getId());
               return badRequest().body(of("message", "ID mismatch between path and request body"));
          }

          // Validate other fields
          if (!isValidJob(job)) {
               logger.error("Invalid job data: {}", job);
               return badRequest().body(of("message", "Invalid job data"));
          }

          try {
               // Call service to update job
               Job updatedJob = service.updateJob(id, job);
               return ok(updatedJob);
          } catch (RuntimeException e) {
               logger.error("Failed to update job with ID {}", id, e);
               if (e.getMessage().contains("not found")) {
                    return status(NOT_FOUND)
                            .body(of("message", "Job not found or you don't have permission to update it"));
               }
               return status(INTERNAL_SERVER_ERROR)
                       .body(of("message", "An error occurred while updating the job"));
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
                    return status(NOT_FOUND).body(of("message", e.getMessage()));
               }
               return status(INTERNAL_SERVER_ERROR).body(of("message", "Failed to delete job"));
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