package edu.uis.csc478.sp25.jobtracker.security;

import edu.uis.csc478.sp25.jobtracker.model.Job;
import edu.uis.csc478.sp25.jobtracker.service.JobService;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static edu.uis.csc478.sp25.jobtracker.security.SecurityUtil.*;

@Component("jobSecurity")
public class JobSecurityExpressionHandler {

     private final JobService jobService;

     public JobSecurityExpressionHandler(JobService jobService) {
          this.jobService = jobService;
     }

     public boolean checkOwner(UUID id) {
          Job job = jobService.getJobById(id).getBody();
          if (job != null) {
               UUID userId = getLoggedInUserId();
               return job.userId.equals(userId);
          }
          return false;
     }
}
