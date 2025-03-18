package edu.uis.csc478.sp25.jobtracker.security;

import edu.uis.csc478.sp25.jobtracker.model.Interview;
import edu.uis.csc478.sp25.jobtracker.service.InterviewService;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static edu.uis.csc478.sp25.jobtracker.security.SecurityUtil.*;

@Component("interviewSecurity")
public class InterviewSecurityExpressionHandler {

     private final InterviewService interviewService;

     public InterviewSecurityExpressionHandler(InterviewService interviewService) {
          this.interviewService = interviewService;
     }

     public boolean checkOwner(UUID id) {
          Interview interview = interviewService.getInterviewById(id).getBody();
          if (interview != null) {
               UUID userId = getLoggedInUserId();
               return interview.user_id.equals(userId);
          }
          return false;
     }
}
