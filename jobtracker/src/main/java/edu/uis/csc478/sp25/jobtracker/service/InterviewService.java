package edu.uis.csc478.sp25.jobtracker.service;

import edu.uis.csc478.sp25.jobtracker.model.Interview;
import edu.uis.csc478.sp25.jobtracker.repository.InterviewRepository;
import edu.uis.csc478.sp25.jobtracker.security.SecurityUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static edu.uis.csc478.sp25.jobtracker.security.SecurityUtil.*;

@Service
public class InterviewService {

     private final InterviewRepository repository;

     public InterviewService(InterviewRepository repository) {
          this.repository = repository;
     }

     public List<Interview> getAllInterviewsForUser() {
          UUID userId = getLoggedInUserId();
          return repository.findByUserId(userId);
     }

     public Interview getInterviewById(UUID id) {
          UUID userId = getLoggedInUserId();
          return repository.findByIdAndUserId(id, userId)
                  .orElseThrow(() -> new RuntimeException("Interview not found or you don't have permission to access it."));
     }

     public List<Interview> searchInterviews(String format, String round, LocalDate date, LocalTime time) {
          UUID userId = getLoggedInUserId();
          return repository.findByFiltersAndUserId(userId, format, round, date, time);
     }

     public Interview createInterview(Interview interview) {
          UUID userId = getLoggedInUserId();

          // Check if an interview with the same ID already exists for this user
          if (repository.existsByIdAndUserId(interview.getId(), userId)) {
               throw new RuntimeException("Interview already exists.");
          }

          // Set the logged-in user's ID
          interview.setUser_id(userId);

          // Save the interview
          return repository.save(interview);
     }


     public Interview updateInterviewById(UUID id, Interview interview) {
          UUID userId = getLoggedInUserId();
          if (!repository.existsByIdAndUserId(id, userId)) {
               throw new RuntimeException("Interview not found or you don't have permission to update it.");
          }
          if (!interview.id.equals(id)) {
               throw new RuntimeException("ID mismatch between path and request body.");
          }
          interview.user_id = userId;
          return repository.save(interview);
     }

     public void deleteInterviewById(UUID id) {
          UUID userId = getLoggedInUserId();
          if (!repository.existsByIdAndUserId(id, userId)) {
               throw new RuntimeException("Interview not found or you don't have permission to delete it.");
          }
          repository.deleteById(id);
     }

     public boolean existsByUUID(UUID id) {
          return repository.existsById(id);
     }
}