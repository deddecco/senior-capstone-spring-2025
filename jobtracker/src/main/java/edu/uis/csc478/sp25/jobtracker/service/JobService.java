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

     /**
      * @param newJob a Job entity
      * @return a ReponseEntity with a status of either CONFLICT or CREATED
      */
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

     /**
      * @param jobID a particular id of a job
      * @return a ReponseEntity with a status of either OK or NOT FOUND
      */
     // Get a specific profile by ID (Admin)
     public ResponseEntity<Job> getJobById(UUID jobID) {
          Optional<Job> job = repository.findById(jobID);

          if (job.isPresent()) {
               return new ResponseEntity<>(job.get(), OK);
          } else {
               return new ResponseEntity<>(NOT_FOUND);
          }
     }

     /**
      * @param jobID the id of the job to be updated
      * @param updatedJob the new job details to take the place of the current values
      *                   for those fields in the job with the given id
      * @return a ReponseEntity that either signals OK or throws an error
      */
     // Update a specific profile by ID (Admin)
     public ResponseEntity<String> updateJobById(UUID jobID,
                                                 Job updatedJob) {
          ResponseEntity<String> result;
          Optional<Job> existingJob = repository.findById(jobID);

          if (existingJob.isPresent()) {
               result = applyJobUpdates(existingJob.get(), updatedJob);
          } else {
               result = new ResponseEntity<>("Job not found. Cannot update job that does not exist", NOT_FOUND);
          }
          return result;
     }

     /**
      * @param existingJob an existing job
      * @param updatedJob an updated job
      * @return a ResponseEntity with a status indicating whether the update happened or not
      */
     // Utility method to copy properties from the updated job to the existing one
     private ResponseEntity<String> applyJobUpdates(Job existingJob,
                                                    Job updatedJob) {
          // Exclude 'id' to prevent overwriting
          copyProperties(updatedJob, existingJob, "id");
          repository.save(existingJob);
          return new ResponseEntity<>("Job updated successfully.", OK);
     }

     /**
      * @param title an optional parameter for job titles
      * @param level an option parameter for values like "entry level" or "senior" or "director" or others
      * @param minSalary an optional parameter for the minimum matching salary
      * @param maxSalary an optional parameter for the maximum matching salary
      * @param location an optional parameter for the location in which the job is advertised
      * @return the jobs that match the filters applied by the paramteres that were given, or all matching jobs the user can see
      */
     public List<Job> searchJobs(String title,
                                 String level,
                                 Integer minSalary,
                                 Integer maxSalary,
                                 String location) {
          return repository.findByFilters(title, level, minSalary, maxSalary, location);
     }

     /**
      * @param jobId of the job to be deleted
      * @return a ResponseEntity with a status of the deletion
      */
     public ResponseEntity<String> deleteJob(UUID jobId) {
          if (!repository.existsById(jobId)) {
               return new ResponseEntity<>("Job not found. Cannot delete job that does not exist",
                       NOT_FOUND);
          }

          repository.deleteById(jobId);
          return new ResponseEntity<>("Job deleted successfully.", OK);
     }

}