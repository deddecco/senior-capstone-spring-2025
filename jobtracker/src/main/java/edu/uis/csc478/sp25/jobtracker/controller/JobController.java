package edu.uis.csc478.sp25.jobtracker.controller;

import edu.uis.csc478.sp25.jobtracker.model.Job;
import edu.uis.csc478.sp25.jobtracker.service.JobService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.Map.of;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.*;
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

     /**
      * @return all jobs visible to the user
      */
     @GetMapping
     public ResponseEntity<List<Job>> getAllJobs() {
          List<Job> userJobs = service.getAllJobsForUser();
          return !userJobs.isEmpty() ? ok(userJobs) : noContent().build();
     }

     /**
      * @param id the id of the job to be retrieved
      * @return a ResponseEntity with either the job or an error if it isn't found
      */
     @GetMapping("/{id}")
     @PreAuthorize("hasRole('ADMIN') or @jobSecurity.checkOwner(#id)")
     public ResponseEntity<Object> getJobById(@PathVariable UUID id) {
          ResponseEntity<Job> jobResponse = service.getJobById(id);
          if (jobResponse.getStatusCode() == OK && jobResponse.getBody() != null) {
               return ok(jobResponse.getBody());
          } else {
               Map<String, Object> errorResponse = of(
                       "status", NOT_FOUND.value(),
                       "error", "Not Found",
                       "message", "A job with ID " + id + " does not exist"
               );
               return new ResponseEntity<>(errorResponse, NOT_FOUND);
          }
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

          List<Job> matchingJobs = service.searchJobs(title, level, minSalary, maxSalary, location);

          return !matchingJobs.isEmpty() ? ok(matchingJobs) : noContent().build();
     }

     /**
      * @return a ResponseEntity with the status-count pairs for the user
      */
     @GetMapping("/status-counts")
     public ResponseEntity<Map<String, Integer>> getJobStatusCounts() {
          Map<String, Integer> statusCounts = service.getJobStatusCounts();
          return ok(statusCounts);
     }

     /**
      * @param job a job object with its properties specified
      * @return a ResponseEntity indicating success or failure of the creation
      */
     @PostMapping
     public ResponseEntity<String> createJob(@RequestBody Job job) {
          try {
               return service.createJob(job);
          } catch (Exception e) {
               return internalServerError().body("Failed to create job: " + e.getMessage());
          }
     }

     /**
      * @param id  the id of the job to be updated
      * @param job the new details
      * @return a ResponseEntity indicating success or failure of the update
      */
     @PutMapping("/{id}")
     @PreAuthorize("hasRole('ADMIN') or @jobSecurity.checkOwner(#id)")
     public ResponseEntity<String> updateJob(@PathVariable UUID id,
                                             @RequestBody Job job) {
          if (!job.id.equals(id)) {
               return badRequest().body("The job ID in the path does not match the ID in the request body.");
          }
          return service.updateJobById(id, job);
     }

     /**
      * @param id of the job to be deleted
      * @return a ResponseEntity indicating success or failure of the deletion of that job
      */
     @DeleteMapping("/{id}")
     @PreAuthorize("hasRole('ADMIN') or @jobSecurity.checkOwner(#id)")
     public ResponseEntity<String> deleteJob(@PathVariable UUID id) {
          try {
               return service.deleteJob(id);
          } catch (Exception e) {
               return internalServerError().body("Failed to delete job: " + e.getMessage());
          }
     }
}