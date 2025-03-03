package edu.uis.csc478.sp25.jobtracker.controller;

import edu.uis.csc478.sp25.jobtracker.model.Job;
import edu.uis.csc478.sp25.jobtracker.service.JobService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.UUID.fromString;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@CrossOrigin
@RestController
@RequestMapping("/jobs")
public class JobController {

     private final JobService service;

     public JobController(JobService service) {
          this.service = service;
     }

     @GetMapping
     public ResponseEntity<List<Job>> getAllJobs() {
          UUID userId = getLoggedInUserId();
          List<Job> userJobs = service.getAllJobsForUser(userId);
          return !userJobs.isEmpty() ? ok(userJobs) : noContent().build();
     }

     @GetMapping("/{id}")
     public ResponseEntity<Object> getJobById(@PathVariable UUID id) {
          UUID userId = getLoggedInUserId();
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

     @GetMapping("/search")
     public ResponseEntity<List<Job>> searchJobs(
             @RequestParam(required = false) String title,
             @RequestParam(required = false) String level,
             @RequestParam(required = false) Integer minSalary,
             @RequestParam(required = false) Integer maxSalary,
             @RequestParam(required = false) String location) {

          UUID userId = getLoggedInUserId();

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

     @PostMapping
     public ResponseEntity<String> createJob(@RequestBody Job job) {
          try {
               UUID userId = getLoggedInUserId();
               job.user_id = userId;
               return service.createJob(job, userId);
          } catch (Exception e) {
               return ResponseEntity.internalServerError().body("Failed to create job: " + e.getMessage());
          }
     }

     @PutMapping("/{id}")
     public ResponseEntity<String> updateJob(@PathVariable UUID id, @RequestBody Job job) {
          UUID userId = getLoggedInUserId();

          if (job.id.equals(id)) {
               if (userId.equals(job.user_id)) {
                    return service.updateJobById(id, job, userId);
               }
               return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have permission to update this job.");
          } else {
               return ResponseEntity.badRequest().body("The job ID in the path does not match the ID in the request body.");
          }

     }

     @DeleteMapping("/{id}")
     public ResponseEntity<String> deleteJob(@PathVariable UUID id) {
          try {
               UUID userId = getLoggedInUserId();
               return service.deleteJob(id, userId);
          } catch (Exception e) {
               return ResponseEntity.internalServerError().body("Failed to delete job: " + e.getMessage());
          }
     }

     private UUID getLoggedInUserId() {
          Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
          if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
               String userId = jwt.getClaim("user_id");
               if (userId != null) {
                    return fromString(userId);
               }
          }
          throw new RuntimeException("No valid authentication found");
     }

     // todo: start counting how many of each status exist
}