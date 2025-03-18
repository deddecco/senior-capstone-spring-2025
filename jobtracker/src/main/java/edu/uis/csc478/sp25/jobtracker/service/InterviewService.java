package edu.uis.csc478.sp25.jobtracker.service;

import edu.uis.csc478.sp25.jobtracker.model.Interview;
import edu.uis.csc478.sp25.jobtracker.repository.InterviewRepository;
import edu.uis.csc478.sp25.jobtracker.security.SecurityUtil;
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
     public List<Interview> getAllInterviewsForUser() {
          UUID userId = SecurityUtil.getLoggedInUserId();
          return repository.findByUserId(userId);
     }

     /**
      * @param id the id of an interview
      * @return a response entity indicating that there are interviews or that there are not
      */
     // GET /interviews/{id}
     public ResponseEntity<Interview> getInterviewById(UUID id) {
          UUID userId = SecurityUtil.getLoggedInUserId();
          Optional<Interview> interviewOptional = repository.findByIdAndUserId(id, userId);

          if (interviewOptional.isPresent()) {
               Interview interview = interviewOptional.get();
               return ResponseEntity.ok(interview);
          } else {
               return ResponseEntity.notFound().build();
          }
     }

     /**
      * @param format the format of the interview-- an optional parameter
      * @param round  the round of the interview-- an optional parameter
      * @param date   the date of the interview-- an optional parameter
      * @param time   the time of the interview -- an optional parameter
      * @return the interviews that match the filters
      */
     // GET /interviews/search
     public List<Interview> searchInterviews(String format,
                                             String round,
                                             LocalDate date,
                                             LocalTime time) {
          UUID userId = SecurityUtil.getLoggedInUserId();
          return repository.findByFiltersAndUserId(userId, format, round, date, time);
     }

     /**
      * @param interview an interview object containing its information, to be put into a sql table with a schema set by the Interview class in the model package
      * @return a ResponseEntity with info about whether the insertion was successful or not
      */
// POST /interviews
     public ResponseEntity<String> createInterview(Interview interview) {
          // Set the user ID
          interview.user_id = SecurityUtil.getLoggedInUserId();
          try {
               repository.save(interview);
               return new ResponseEntity<>("Interview created successfully!", CREATED);
          } catch (Exception e) {
               // Handle any exceptions that occur during saving, such as database errors
               return new ResponseEntity<>("Failed to create interview: " + e.getMessage(), INTERNAL_SERVER_ERROR);
          }
     }

     /**
      * @param id        the primary key of the interview table, identifying an interview
      * @param interview an interview object containing the properties of an interview
      * @return a ResponseEntity indicating the success or failure of the update
      */
     // PUT /interviews/{id}
     public ResponseEntity<String> updateInterviewById(UUID id, Interview interview) {
          UUID userId = SecurityUtil.getLoggedInUserId();
          if (repository.existsByIdAndUserId(id, userId)) {
               if (!interview.id.equals(id)) {
                    return badRequest().body("ID mismatch between path and request body.");
               }
               // Ensure user ID matches
               interview.user_id = userId;
               repository.save(interview);
               return ok("Interview updated successfully");
          } else {
               return new ResponseEntity<>("Interview not found or you don't have permission to update it.", NOT_FOUND);
          }
     }

     /**
      * @param id the primary key identifying the interview to be deleted
      * @return a ResponseEntity with info about the success or failure of deletion
      */
     // DELETE /interviews/{id}
     public ResponseEntity<String> deleteInterviewById(UUID id) {
          UUID userId = SecurityUtil.getLoggedInUserId();
          if (repository.existsByIdAndUserId(id, userId)) {
               repository.deleteById(id);
               return ok("Interview deleted successfully");
          } else {
               return new ResponseEntity<>("Interview not found or you don't have permission to delete it.", NOT_FOUND);
          }
     }

}