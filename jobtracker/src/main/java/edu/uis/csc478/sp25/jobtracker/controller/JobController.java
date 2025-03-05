package edu.uis.csc478.sp25.jobtracker.controller;

import edu.uis.csc478.sp25.jobtracker.auth.UserIdExtractor;
import edu.uis.csc478.sp25.jobtracker.model.Job;
import edu.uis.csc478.sp25.jobtracker.service.JobService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@CrossOrigin
@RestController
@RequestMapping("/jobs")
public class JobController {

     private final JobService service;
     private final UserIdExtractor userIdExtractor;

     public JobController(JobService service, UserIdExtractor userIdExtractor) {
          this.service = service;
          this.userIdExtractor = userIdExtractor;
     }

     /**
      * @return all jobs visible to the user
      */
     @GetMapping
     public ResponseEntity<List<Job>> getAllJobs() {
          UUID userId = userIdExtractor.getLoggedInUserId();
          List<Job> userJobs = service.getAllJobsForUser(userId);
          return !userJobs.isEmpty() ? ok(userJobs) : noContent().build();
     }

     /**
      * @param id the id of the job to be retrieved
      * @return a ResponseEntity with either the job or an error if it isn't found
      */
     @GetMapping("/{id}")
     public ResponseEntity<Object> getJobById(@PathVariable UUID id) {
          UUID userId = userIdExtractor.getLoggedInUserId();
          ResponseEntity<Job> jobResponse = service.getJobById(id, userId);

          if (jobResponse.getStatusCode() == HttpStatus.OK && jobResponse.getBody() != null) {
               return ResponseEntity.ok(jobResponse.getBody());
          }

          Map<String, Object> errorResponse = new HashMap<>();
          errorResponse.put("status", NOT_FOUND.value());
          errorResponse.put("error", "Not Found");
          errorResponse.put("message", "A job with ID " + id + " does not exist or you don't have permission to view it");

          return new ResponseEntity<>(errorResponse, NOT_FOUND);
     }

     /**
      * @param title     optional search parameter
      * @param level     optional search parameter
      * @param minSalary optional search parameter
      * @param maxSalary optional search parameter
      * @param location  optional search parameter
      * @return the jobs that match the provided parameters, or all matching jobs if none specified
      */
     @GetMapping("/search")
     public ResponseEntity<List<Job>> searchJobs(
             @RequestParam(required = false) String title,
             @RequestParam(required = false) String level,
             @RequestParam(required = false) Integer minSalary,
             @RequestParam(required = false) Integer maxSalary,
             @RequestParam(required = false) String location) {

          UUID userId = userIdExtractor.getLoggedInUserId();

          List<Job> matchingJobs = service.searchJobs(
                  userId,
                  title,
                  level,
                  minSalary,
                  maxSalary,
                  location
          );

          return !matchingJobs.isEmpty() ? ok(matchingJobs) : noContent().build();
     }

     /**
      * @return a ResponseEntity with the status-count pairs for the user
      */
     @GetMapping("/status-counts")
     public ResponseEntity<Map<String, Integer>> getJobStatusCounts() {
          UUID userId = userIdExtractor.getLoggedInUserId();
          Map<String, Integer> statusCounts = service.getJobStatusCounts(userId);
          return ResponseEntity.ok(statusCounts);
     }


     /**
      * @param job a job object with its properties specified
      * @return a ResponseEntity indicating success or failure of the creation
      */
     @PostMapping
     public ResponseEntity<String> createJob(@RequestBody Job job) {
          try {
               UUID userId = userIdExtractor.getLoggedInUserId();
               job.userId = userId;
               return service.createJob(job, userId);
          } catch (Exception e) {
               return ResponseEntity.internalServerError().body("Failed to create job: " + e.getMessage());
          }
     }

     /**
      * @param id  the id of the job to be updated
      * @param job the new details
      * @return a ResponseEntity indicating success or failure of the update
      */
     @PutMapping("/{id}")
     public ResponseEntity<String> updateJob(@PathVariable UUID id,
                                             @RequestBody Job job) {
          UUID userId = userIdExtractor.getLoggedInUserId();

          if (job.id.equals(id)) {
               if (userId.equals(job.userId)) {
                    return service.updateJobById(id, job, userId);
               }
               return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have permission to update this job.");
          } else {
               return ResponseEntity.badRequest().body("The job ID in the path does not match the ID in the request body.");
          }
     }

     /**
      * @param id of the job to be deleted
      * @return a ResponseEntity indicating success or failure of the deletion of that job
      */
     @DeleteMapping("/{id}")
     public ResponseEntity<String> deleteJob(@PathVariable UUID id) {
          try {
               UUID userId = userIdExtractor.getLoggedInUserId();
               return service.deleteJob(id, userId);
          } catch (Exception e) {
               return ResponseEntity.internalServerError().body("Failed to delete job: " + e.getMessage());
          }
     }
}