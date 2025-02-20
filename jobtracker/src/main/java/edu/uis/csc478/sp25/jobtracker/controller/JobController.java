package edu.uis.csc478.sp25.jobtracker.controller;

import edu.uis.csc478.sp25.jobtracker.model.Job;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/jobs")
public class JobController {

     /// / USER ////

     // GET /jobs
     @GetMapping
     public ResponseEntity<List<Job>> getAllJobs() {
          // Implementation
          return null;
     }

     // GET /jobs/{id}
     @GetMapping("/{id}")
     public ResponseEntity<Job> getJobById(@PathVariable UUID id) {
          // Implementation
          return null;
     }

     // GET /jobs/search
     @GetMapping("/search")
     public ResponseEntity<List<Job>> searchJobs(
             @RequestParam(required = false) String title,
             @RequestParam(required = false) String level,
             @RequestParam(required = false) Integer minSalary,
             @RequestParam(required = false) Integer maxSalary,
             @RequestParam(required = false) String location) {
          // Implementation
          return null;
     }

     /// / ADMIN /////

     // POST /jobs
     @PostMapping
     public ResponseEntity<Job> createJob(@RequestBody Job job) {
          // Implementation
          return null;
     }

     // PUT /jobs/{id}
     @PutMapping("/{id}")

     public ResponseEntity<Job> updateJob(@PathVariable UUID id, @RequestBody Job job) {
          // Implementation
          return null;
     }

     // DELETE /admin/jobs/{id}
     @DeleteMapping("/{id}")
     public ResponseEntity<Void> deleteJob(@PathVariable UUID id) {
          // Implementation
          return null;
     }
}