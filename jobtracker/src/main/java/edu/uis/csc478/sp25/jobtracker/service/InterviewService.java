package edu.uis.csc478.sp25.jobtracker.service;

import edu.uis.csc478.sp25.jobtracker.model.Interview;
import edu.uis.csc478.sp25.jobtracker.repository.InterviewRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InterviewService {

     private final InterviewRepository repository;

     public InterviewService(InterviewRepository repository) {
          this.repository = repository;
     }

     public List<Interview> getAllInterviews() {
          return (List<Interview>) repository.findAll();
     }

     public ResponseEntity<List<Interview>> getInterviewsByID(UUID userId) {
          List<Interview> interviews = repository.findByUserId(userId);
          if (interviews.isEmpty()) {
               return ResponseEntity.noContent().build();
          }
          return ResponseEntity.ok(interviews);
     }

     public List<Interview> searchInterviews(UUID userId,
                                             String format,
                                             String round,
                                             String date,
                                             String time) {
          return repository.findByFilters(userId,
                  format,
                  round,
                  date,
                  time);
     }

     public boolean existsByUUID(UUID id) {
          return repository.existsById(id);
     }

     public void createInterview(Interview interview) {
          repository.save(interview);
     }

     public ResponseEntity<Interview> getInterviewById(UUID id) {
          Optional<Interview> interview = repository.findById(id);
          return interview.map(ResponseEntity::ok)
                  .orElseGet(() -> ResponseEntity.notFound().build());
     }

     public ResponseEntity<String> updateInterviewById(UUID id,
                                                       Interview updatedInterview) {
          if (!repository.existsById(id)) {
               return ResponseEntity.notFound().build();
          }
          updatedInterview.setId(id); // Ensure the ID is set correctly
          repository.save(updatedInterview);
          return ResponseEntity.ok("Interview updated successfully");
     }

     public ResponseEntity<String> deleteInterviewById(UUID id) {
          if (!repository.existsById(id)) {
               return ResponseEntity.notFound().build();
          }
          repository.deleteById(id);
          return ResponseEntity.ok("Interview deleted successfully");
     }
}