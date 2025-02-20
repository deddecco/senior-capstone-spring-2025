package edu.uis.csc478.sp25.jobtracker.service;

import edu.uis.csc478.sp25.jobtracker.model.Job;
import edu.uis.csc478.sp25.jobtracker.repository.JobRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.beans.BeanUtils.copyProperties;
import static org.springframework.http.HttpStatus.*;

@Service
public class JobService {
     private final JobRepository repository;

     public JobService(JobRepository repository) {
          this.repository = repository;
     }

     public List<Job> getAllJobs() {
          return (List<Job>) repository.findAll();
     }

     // create a new job, assuming one with the same job id does not yet exist
     public ResponseEntity<String> createJob(Job newJob) {
          if (existsByUUID(newJob.id)) {
               return new ResponseEntity<>("Job already exists.", CONFLICT);
          }
          repository.save(newJob);
          return new ResponseEntity<>("Job created successfully.", CREATED);
     }

     // Check if a profile exists by ID
     public boolean existsByUUID(UUID jobID) {
          return repository.existsById(jobID);
     }


     // Get a specific profile by ID (Admin)
     public ResponseEntity<Job> getJobById(UUID jobID) {
          Optional<Job> job = repository.findById(jobID);

          if (job.isPresent()) {
               return new ResponseEntity<>(job.get(), OK);
          } else {
               return new ResponseEntity<>(NOT_FOUND);
          }
     }


     // Update a specific profile by ID (Admin)
     public ResponseEntity<String> updateJobById(UUID jobID, Job updatedJob) {
          Optional<Job> existingJob = repository.findById(jobID);

          if (existingJob.isPresent()) {
               return applyProfileUpdates(existingJob.get(), updatedJob);
          } else {
               return new ResponseEntity<>("Job not found.", NOT_FOUND);
          }
     }

     // Utility method to copy properties from the updated job to the existing one
     private ResponseEntity<String> applyProfileUpdates(Job existingJob, Job updatedJob) {
          // Exclude 'id' to prevent overwriting
          copyProperties(updatedJob, existingJob, "id");
          repository.save(existingJob);
          return new ResponseEntity<>("Profile updated successfully.", OK);
     }

     public List<Job> searchJobs(String title,
                                 String level,
                                 Integer minSalary,
                                 Integer maxSalary,
                                 String location) {
          return repository.findByFilters(title, level, minSalary, maxSalary, location);
     }

     public ResponseEntity<String> deleteJob(UUID jobId) {
          if (!repository.existsById(jobId)) {
               return new ResponseEntity<>("Job not found.", NOT_FOUND);
          }

          repository.deleteById(jobId);
          return new ResponseEntity<>("Job deleted successfully.", OK);
     }

}
