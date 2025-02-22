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
     public List<Interview> getAllInterviews() {
          return (List<Interview>) repository.findAll();
     }

     // GET /interviews/{id}
     public ResponseEntity<Interview> getInterviewById(UUID id) {
          Optional<Interview> interviewOptional = repository.findById(id);
          if (interviewOptional.isPresent()) {
               return ok(interviewOptional.get());
          }
          return ResponseEntity.notFound().build();
     }

     // GET /interviews/search
     public List<Interview> searchInterviews(UUID userId, String format, String round, LocalDate date, LocalTime time) {
          return repository.findByFilters(userId, format, round, date, time);
     }

     // POST /interviews
     public ResponseEntity<String> createInterview(Interview interview) {
          if (!existsByUUID(interview.getId())) {
               repository.save(interview);
               return new ResponseEntity<>("Interview created successfully!", CREATED);
          } else {
               return new ResponseEntity<>("Interview already exists.", CONFLICT);
          }
     }

     // PUT /interviews/{id}
     public ResponseEntity<String> updateInterviewById(UUID id, Interview interview) {
          if (repository.existsById(id)) {
               if (!interview.getId().equals(id)) {
                    return badRequest().body("ID mismatch between path and request body.");
               }
               repository.save(interview);
               return ok("Interview updated successfully");
          } else {
               return new ResponseEntity<>("Interview not found.", NOT_FOUND);
          }
     }

     // DELETE /interviews/{id}
     public ResponseEntity<String> deleteInterviewById(UUID id) {
          if (repository.existsById(id)) {
               repository.deleteById(id);
               return ok("Interview deleted successfully");
          } else {
               return new ResponseEntity<>("Interview not found.", NOT_FOUND);
          }
     }

     // Helper method for existence check
     public boolean existsByUUID(UUID id) {
          return repository.existsById(id);
     }
}
