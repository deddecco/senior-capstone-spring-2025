package edu.uis.csc478.sp25.jobtracker.service;

import edu.uis.csc478.sp25.jobtracker.model.Job;
import edu.uis.csc478.sp25.jobtracker.repository.JobRepository;
import edu.uis.csc478.sp25.jobtracker.security.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.UUID.*;
import static org.springframework.beans.BeanUtils.copyProperties;
import static org.springframework.http.HttpStatus.*;

@Service
public class JobService {

     private final JobRepository repository;

     public JobService(JobRepository repository) {
          this.repository = repository;
     }

     // Helper method to get the logged-in user's ID
     private UUID getLoggedInUserId() {
          return SecurityUtil.getLoggedInUserId();
     }

     public List<Job> getAllJobsForUser() {
          return repository.findAllByUserId(getLoggedInUserId());
     }

     /**
      * @param newJob a Job entity
      * @return a ResponseEntity with a status of either CONFLICT or CREATED
      */
     public ResponseEntity<String> createJob(Job newJob) {
          UUID userId = getLoggedInUserId();

          if (newJob.id != null && repository.existsById(newJob.id)) {
               return new ResponseEntity<>("Job already exists.", CONFLICT);
          }

          if (newJob.id == null) {
               newJob.id = randomUUID();
          }

          newJob.userId = userId;
          repository.save(newJob);
          return new ResponseEntity<>("Job created successfully.", CREATED);
     }

     /**
      * @param jobID a particular id of a job
      * @return a ResponseEntity with a status of either OK or NOT FOUND
      */
     public ResponseEntity<Job> getJobById(UUID jobID) {
          UUID userId = getLoggedInUserId();
          Optional<Job> job = repository.findByIdAndUserId(jobID, userId);

          if (job.isPresent()) {
               return new ResponseEntity<>(job.get(), OK);
          } else {
               return new ResponseEntity<>(NOT_FOUND);
          }
     }

     /**
      * @param jobID      the id of the job to be updated
      * @param updatedJob the new job details to take the place of the current values
      * @return a ResponseEntity that either signals OK or throws an error
      */
     public ResponseEntity<String> updateJobById(UUID jobID, Job updatedJob) {
          UUID userId = getLoggedInUserId();
          Optional<Job> existingJob = repository.findByIdAndUserId(jobID, userId);

          if (existingJob.isPresent()) {
               return applyJobUpdates(existingJob.get(), updatedJob);
          } else {
               return new ResponseEntity<>("Job not found or you don't have permission to update it.", NOT_FOUND);
          }
     }

     /**
      * @param existingJob an existing job
      * @param updatedJob  an updated job
      * @return a ResponseEntity with a status indicating whether the update happened or not
      */
     private ResponseEntity<String> applyJobUpdates(Job existingJob, Job updatedJob) {
          copyProperties(updatedJob, existingJob, "id", "userId");
          repository.save(existingJob);
          return new ResponseEntity<>("Job updated successfully.", OK);
     }

     /**
      * @param title     an optional parameter for job titles
      * @param level     an optional parameter for values like "entry level" or "senior" or "director" or others
      * @param minSalary an optional parameter for the minimum matching salary
      * @param maxSalary an optional parameter for the maximum matching salary
      * @param location  an optional parameter for the location in which the job is advertised
      * @return the jobs that match the filters applied by the parameters that were given, including the user's jobs and potentially other visible jobs
      */
     public List<Job> searchJobs(String title, String level, Integer minSalary, Integer maxSalary, String location) {
          return repository.findByFilters(getLoggedInUserId(), title, level, minSalary, maxSalary, location);
     }

     /**
      * @param jobId of the job to be deleted
      * @return a ResponseEntity with a status of the deletion
      */
     public ResponseEntity<String> deleteJob(UUID jobId) {
          UUID userId = getLoggedInUserId();
          if (!repository.existsByIdAndUserId(jobId, userId)) {
               return new ResponseEntity<>("Job not found or you don't have permission to delete it.", NOT_FOUND);
          }

          repository.deleteById(jobId);
          return new ResponseEntity<>("Job deleted successfully.", OK);
     }

     public Map<String, Integer> getJobStatusCounts() {
          List<Object[]> results = repository.countJobsByStatus(getLoggedInUserId());
          Map<String, Integer> statusCounts = new HashMap<>();

          // Initialize all statuses with 0 count
          String[] statuses = {"Saved", "Applied", "Screening", "Interview", "Rejected", "Offer", "Hired"};
          for (String status : statuses) {
               statusCounts.put(status, 0);
          }

          // Update counts from the query results
          for (Object[] result : results) {
               String status = (String) result[0];
               Integer count = ((Number) result[1]).intValue();
               statusCounts.put(status, count);
          }

          return statusCounts;
     }
}
