package edu.uis.csc478.sp25.jobtracker.service;

import edu.uis.csc478.sp25.jobtracker.model.Interview;
import edu.uis.csc478.sp25.jobtracker.repository.InterviewRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@Service
public class InterviewService {

     private final InterviewRepository repository;

     public InterviewService(InterviewRepository repository) {
          this.repository = repository;
     }

     // GET /interviews
     public List<Interview> getAllInterviewsForUser(UUID userId) {
          return repository.findByUserId(userId);
     }

     // GET /interviews/{id}
     public ResponseEntity<Interview> getInterviewByIdForUser(UUID id, UUID userId) {
          Optional<Interview> interviewOptional = repository.findByIdAndUserId(id, userId);

          if (interviewOptional.isPresent()) {
               Interview interview = interviewOptional.get();
               return ResponseEntity.ok(interview);
          } else {
               return ResponseEntity.notFound().build();
          }
     }


     // GET /interviews/search
     public List<Interview> searchInterviewsForUser(UUID userId, String format, String round, LocalDate date, LocalTime time) {
          return repository.findByFiltersAndUserId(userId, format, round, date, time);
     }

     // POST /interviews
     public ResponseEntity<String> createInterview(Interview interview, UUID userId) {
          if (!existsByUUID(interview.id)) {
               interview.user_id = userId;
               repository.save(interview);
               return new ResponseEntity<>("Interview created successfully!", CREATED);
          } else {
               return new ResponseEntity<>("Interview already exists.", CONFLICT);
          }
     }

     // PUT /interviews/{id}
     public ResponseEntity<String> updateInterviewById(UUID id, Interview interview, UUID userId) {
          if (repository.existsByIdAndUserId(id, userId)) {
               if (!interview.id.equals(id)) {
                    return badRequest().body("ID mismatch between path and request body.");
               }
               interview.user_id = userId;
               repository.save(interview);
               return ok("Interview updated successfully");
          } else {
               return new ResponseEntity<>("Interview not found or you don't have permission to update it.", NOT_FOUND);
          }
     }

     // DELETE /interviews/{id}
     public ResponseEntity<String> deleteInterviewById(UUID id, UUID userId) {
          if (repository.existsByIdAndUserId(id, userId)) {
               repository.deleteById(id);
               return ok("Interview deleted successfully");
          } else {
               return new ResponseEntity<>("Interview not found or you don't have permission to delete it.", NOT_FOUND);
          }
     }

     // Helper method for existence check
     public boolean existsByUUID(UUID id) {
          return repository.existsById(id);
     }

     // Helper method to check if an interview belongs to a user
     public boolean interviewBelongsToUser(UUID id, UUID userId) {
          return repository.existsByIdAndUserId(id, userId);
     }
}