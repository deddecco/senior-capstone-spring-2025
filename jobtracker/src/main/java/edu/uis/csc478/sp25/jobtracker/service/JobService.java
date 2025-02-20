package edu.uis.csc478.sp25.jobtracker.service;

import edu.uis.csc478.sp25.jobtracker.repository.JobRepository;
import org.springframework.stereotype.Service;

@Service
public class JobService {
     private final JobRepository repository;

     public JobService(JobRepository repository) {
          this.repository = repository;
     }

     //// USER ////

     // GET /jobs

     // GET /jobs/{id}

     // GET /jobs/search

     //// ADMIN /////

     // POST /jobs

     // PUT /jobs/{id}

     // DELETE /admin/jobs/{id}
}
