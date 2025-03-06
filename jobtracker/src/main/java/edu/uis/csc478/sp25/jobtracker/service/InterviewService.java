package edu.uis.csc478.sp25.jobtracker.service;

import edu.uis.csc478.sp25.jobtracker.model.Interview;
import edu.uis.csc478.sp25.jobtracker.repository.InterviewRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
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

     /**
      * @param id     the id of an interview
      * @param userId the id of the user whose interview records will be fetched
      * @return a response entity indicating that there are interviews or that there are not
      */
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


     /**
      * @param userId the id of the user the interview is for
      * @param format the format of the interview-- an optional parameter
      * @param round the round of the interview-- an optional parameter
      * @param date the date of the interview-- an optional parameter
      * @param time the time of the interview -- an optional parameter
      * @return the interviews that match the filters
      */
     // GET /interviews/search
     public List<Interview> searchInterviewsForUser(UUID userId,
                                                    String format,
                                                    String round,
                                                    LocalDate date,
                                                    LocalTime time) {
          return repository.findByFiltersAndUserId(userId, format, round, date, time);
     }

     /**
      * @param interview an interview object containing its information, to be put into a sql table with a schema set by the Interview class in the model package
      * @param userId    the id of the user the interview is for
      * @return a ResponseEntity with info about whether the insertion was successful or not
      */
     // POST /interviews
     public ResponseEntity<String> createInterview(Interview interview, UUID userId) {
          if (!existsByUUID(interview.id)) {
               if (!interviewBelongsToUser(interview.id, userId)) {
                    return new ResponseEntity<>("Cannot create interviews for someone else", FORBIDDEN);
               }
               repository.save(interview);
               return new ResponseEntity<>("Interview created successfully!", CREATED);
          } else {
               return new ResponseEntity<>("Interview already exists.", CONFLICT);
          }
     }

     /**
      * @param id        the primary key of the interview table, identifying an interview
      * @param interview an interview object containing the properties of an interview
      * @param userId    the id of the user for whom the interview is scheduled
      * @return a ResponseEntity indicating the success or failure of the update
      */
     // PUT /interviews/{id}
     public ResponseEntity<String> updateInterviewById(UUID id,
                                                       Interview interview,
                                                       UUID userId) {
          if (repository.existsByIdAndUserId(id, userId)) {
               if (!interview.id.equals(id)) {
                    return badRequest().body("ID mismatch between path and request body.");
               } else if (!interviewBelongsToUser(id, userId)) {
                    return new ResponseEntity<>("Cannot update someone else's interview", FORBIDDEN);
               }
               interview.user_id = userId;
               repository.save(interview);
               return ok("Interview updated successfully");
          } else {
               return new ResponseEntity<>("Interview not found or you don't have permission to update it.", NOT_FOUND);
          }
     }

     /**
      * @param id     the primary key identifying the interview to be deleted
      * @param userId the user id to validate that a user is only deleting their own interviews
      * @return a ResponseEntity with info about the success or failure of deletion
      */
     // DELETE /interviews/{id}
     public ResponseEntity<String> deleteInterviewById(UUID id,
                                                       UUID userId) {
          if (repository.existsByIdAndUserId(id, userId)) {
               if (!interviewBelongsToUser(id, userId)) {
                    return new ResponseEntity<>("cannot delete someone else's interviews.", FORBIDDEN);
               }
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

     public UUID getLoggedInUserId() {
          Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
          if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
               String userId = jwt.getClaim("user_id");
               if (userId != null) {
                    return UUID.fromString(userId);
               }
          }
          throw new RuntimeException("No valid authentication found");
     }
}