/*
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

import static java.util.Map.*;
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
               List<Job> userJobs = service.getAllJobsForUser();
               return userJobs.isEmpty() ? noContent().build() : ok(userJobs);
          } catch (Exception e) {
               logger.error("Error fetching jobs for user", e);
               return status(HttpStatus.INTERNAL_SERVER_ERROR).build();
          }
     }

     @GetMapping("/{id}")
     public ResponseEntity<Object> getJobById(@PathVariable UUID id) {
          try {
               ResponseEntity<Job> serviceResponse = service.getJobById(id);
               if (serviceResponse.getStatusCode() == HttpStatus.OK) {
                    return ok(serviceResponse.getBody());
               } else {
                    return status(HttpStatus.NOT_FOUND)
                            .body(of("message", "Job not found or you don't have permission to view it"));
               }
          } catch (Exception e) {
               logger.error("Error fetching job by ID", e);
               return status(HttpStatus.INTERNAL_SERVER_ERROR)
                       .body(of("message", "An error occurred while fetching the job"));
          }
     }


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
               return status(HttpStatus.INTERNAL_SERVER_ERROR).build();
          }
     }

     @GetMapping("/status-counts")
     public ResponseEntity<Map<String, Integer>> getJobStatusCounts() {
          try {
               Map<String, Integer> statusCounts = service.getJobStatusCounts();
               return ok(statusCounts);
          } catch (Exception e) {
               logger.error("Error fetching job status counts", e);
               return status(HttpStatus.INTERNAL_SERVER_ERROR).build();
          }
     }

     @PostMapping
     public ResponseEntity<String> createJob(@RequestBody Job job) {
          if (!isValidJob(job)) {
               return badRequest().body("Invalid job data");
          }
          try {
               return service.createJob(job);
          } catch (Exception e) {
               logger.error("Failed to create job", e);
               return status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create job");
          }
     }

     @PutMapping("/{id}")
     public ResponseEntity<String> updateJob(@PathVariable UUID id, @RequestBody Job job) {
          if (!isValidJob(job) || !job.getId().equals(id)) {
               return badRequest().body("Invalid job data or ID mismatch");
          }
          try {
               return service.updateJobById(id, job);
          } catch (Exception e) {
               logger.error("Failed to update job with ID {}", id, e);
               return status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update job");
          }
     }

     @DeleteMapping("/{id}")
     public ResponseEntity<String> deleteJob(@PathVariable UUID id) {
          try {
               return service.deleteJob(id);
          } catch (Exception e) {
               logger.error("Failed to delete job with ID {}", id, e);
               return status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete job");
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
*/
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
               return status(HttpStatus.INTERNAL_SERVER_ERROR).build();
          }
     }

     @GetMapping("/{id}")
     public ResponseEntity<Object> getJobById(@PathVariable UUID id) {
          try {
               ResponseEntity<Job> serviceResponse = service.getJobById(id);
               if (serviceResponse.getStatusCode() == HttpStatus.OK) {
                    return ok(serviceResponse.getBody());
               } else {
                    return status(HttpStatus.NOT_FOUND)
                            .body(of("message", "Job not found or you don't have permission to view it"));
               }
          } catch (Exception e) {
               logger.error("Error fetching job by ID", e);
               return status(HttpStatus.INTERNAL_SERVER_ERROR)
                       .body(of("message", "An error occurred while fetching the job"));
          }
     }

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
               return status(HttpStatus.INTERNAL_SERVER_ERROR).build();
          }
     }

     @GetMapping("/status-counts")
     public ResponseEntity<Map<String, Integer>> getJobStatusCounts() {
          try {
               Map<String, Integer> statusCounts = service.getJobStatusCounts();
               return ok(statusCounts);
          } catch (Exception e) {
               logger.error("Error fetching job status counts", e);
               return status(HttpStatus.INTERNAL_SERVER_ERROR).build();
          }
     }

     @PostMapping
     public ResponseEntity<String> createJob(@RequestBody Job job) {
          if (!isValidJob(job)) {
               return badRequest().body("Invalid job data");
          }
          try {
               return service.createJob(job);
          } catch (Exception e) {
               logger.error("Failed to create job", e);
               return status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create job");
          }
     }

     @PutMapping("/{id}")
     public ResponseEntity<String> updateJob(@PathVariable UUID id, @RequestBody Job job) {
          if (!isValidJob(job) || !job.getId().equals(id)) {
               return badRequest().body("Invalid job data or ID mismatch");
          }
          try {
               return service.updateJob(id, job);
          } catch (Exception e) {
               logger.error("Failed to update job with ID {}", id, e);
               return status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update job");
          }
     }

     @DeleteMapping("/{id}")
     public ResponseEntity<String> deleteJob(@PathVariable UUID id) {
          try {
               return service.deleteJob(id);
          } catch (Exception e) {
               logger.error("Failed to delete job with ID {}", id, e);
               return status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete job");
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
