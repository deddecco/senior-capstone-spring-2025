package edu.uis.csc478.sp25.jobtracker.controller;

import edu.uis.csc478.sp25.jobtracker.model.Job;
import edu.uis.csc478.sp25.jobtracker.service.JobService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.*;

@CrossOrigin
@RestController
@RequestMapping("/jobs")
public class JobController {

     private final JobService service;

     public JobController(JobService service) {
          this.service = service;
     }

     /// / USER ////

     // GET /jobs
     @GetMapping
     public ResponseEntity<List<Job>> getAllJobs() {
          List<Job> jobs = service.getAllJobs();
          boolean matchingJobsExist = !jobs.isEmpty();
          return matchingJobsExist ? ok(jobs) : noContent().build();
     }

     // GET /jobs/{id}
     @GetMapping("/{id}")
     public ResponseEntity<Object> getJobById(@PathVariable UUID id) {
          ResponseEntity<Job> jobResponse = service.getJobById(id);
          if (jobResponse.getBody() != null) {
               return ok(jobResponse.getBody());
          }
          Map<String, Object> errorResponse = new HashMap<>();
          errorResponse.put("status", NOT_FOUND.value());
          errorResponse.put("error", "Not Found");
          errorResponse.put("message", "A job with ID " + id + " does not exist");

          return new ResponseEntity<>(errorResponse, NOT_FOUND);
     }

     // GET /jobs/search
     @GetMapping("/search")
     public ResponseEntity<List<Job>> searchJobs(
             // none of these params are required; you can search for a job with any subset of these
             @RequestParam(required = false) String title,
             @RequestParam(required = false) String level,
             @RequestParam(required = false) Integer minSalary,
             @RequestParam(required = false) Integer maxSalary,
             @RequestParam(required = false) String location) {
          List<Job> matchingJobs = service.searchJobs(title,
                  level,
                  minSalary,
                  maxSalary,
                  location);

          // the request was successful but found no matches
          if (matchingJobs.isEmpty()) {
               return noContent().build();
          }
          // the request was successful and found matches
          return ok(matchingJobs);
     }

     /// / ADMIN /////

     // POST /jobs
     @PostMapping
     public ResponseEntity<String> createJob(@RequestBody Job job) {
          try {
               // Check if the email already exists
               if (service.existsByUUID(job.getId())) {
                    return new ResponseEntity<>("Job already exists.", CONFLICT);
               }
               service.createJob(job);
               // Use CREATED status for creation
               return new ResponseEntity<>("Job created successfully!", CREATED);
          } catch (Exception e) {
               return internalServerError().body("Failed to create job: " + e.getMessage());
          }
     }

     // PUT /jobs/{id}
     @PutMapping("/{id}")
     public ResponseEntity<String> updateJob(@PathVariable UUID id,
                                             @RequestBody Job job) {
          // if the job id in the path and request do not match, then the request should not go through
          // because those two things deal with different jobs, and using one to update the other would be a mistake
          if (!job.getId().equals(id)) {
               return badRequest().body("The job ID in the path does not match the ID in the request body.");
          }

          return service.updateJobById(id, job);
     }

     // DELETE /jobs/{id}
     @DeleteMapping("/{id}")
     public ResponseEntity<Void> deleteJob(@PathVariable UUID id) {
          // delete a job if it exists
          try {
               service.deleteJob(id);
               return noContent().build();
          }
          // do not allow the deletion of a job that does not exist
          catch (Exception e) {
               return status(HttpStatus.INTERNAL_SERVER_ERROR).build();
          }
     }

}