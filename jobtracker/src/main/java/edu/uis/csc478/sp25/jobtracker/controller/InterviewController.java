package edu.uis.csc478.sp25.jobtracker.controller;

import edu.uis.csc478.sp25.jobtracker.model.Interview;
import edu.uis.csc478.sp25.jobtracker.service.InterviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.UUID.fromString;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.*;

@CrossOrigin
@RestController
@RequestMapping("/interviews")
public class InterviewController {

     private final InterviewService service;

     public InterviewController(InterviewService service) {
          this.service = service;
     }

     /**
      * @return a response entity with either an OK code and a list of all interviews associated wth a user, or noContent if there are none
      */
     @GetMapping
     public ResponseEntity<List<Interview>> getAllInterviews() {
          UUID userId = getLoggedInUserId();
          List<Interview> interviews = service.getAllInterviewsForUser(userId);
          return !interviews.isEmpty() ? ok(interviews) : noContent().build();
     }


     /**
      * @param id to find an interview
      * @return that interview to be displayed by the frontend
      */
     @GetMapping("/{id}")
     public ResponseEntity<Object> getInterviewById(@PathVariable UUID id) {
          UUID userId = getLoggedInUserId();
          ResponseEntity<Interview> interviewResponse = service.getInterviewByIdForUser(id, userId);
          if (interviewResponse.getStatusCode().is2xxSuccessful()) {
               return ok(interviewResponse.getBody());
          }
          Map<String, Object> errorResponse = new HashMap<>();
          errorResponse.put("status", NOT_FOUND.value());
          errorResponse.put("error", "Not Found");
          errorResponse.put("message", "An interview with ID " + id + " does not exist or you don't have access to it");
          return new ResponseEntity<>(errorResponse, NOT_FOUND);
     }

     /**
      * @param format optional parameter for the search
      * @param round optional parameter for the search
      * @param date optional parameter for the search
      * @param time optional parameter for the search
      * @return all the interviews that match any/all the parameters given in teh query
      */
     @GetMapping("/search")
     public ResponseEntity<List<Interview>> searchInterviews(
             @RequestParam(required = false) String format,
             @RequestParam(required = false) String round,
             @RequestParam(required = false) LocalDate date,
             @RequestParam(required = false) LocalTime time) {
          UUID userId = getLoggedInUserId();
          List<Interview> matchingInterviews = service.searchInterviewsForUser(userId, format, round, date, time);
          return !matchingInterviews.isEmpty() ? ok(matchingInterviews) : noContent().build();
     }

     /**
      * @param interview an interview
      * @return either that an interview was created, or an error explaining why not
      */
     @PostMapping
     public ResponseEntity<String> createInterview(@RequestBody Interview interview) {
          try {
               UUID userId = getLoggedInUserId();
               return service.createInterview(interview, userId);
          } catch (Exception e) {
               return internalServerError().body("Failed to create interview: " + e.getMessage());
          }
     }

     /**
      * @param id of an interview
      * @param interview replacement values for the fields of that interview-- do not change ids
      * @return either that the interview was updated, or an error explaining why not
      */
     @PutMapping("/{id}")
     public ResponseEntity<String> updateInterview(@PathVariable UUID id,
                                                   @RequestBody Interview interview) {
          UUID userId = getLoggedInUserId();
          if (!interview.id.equals(id)) {
               return badRequest().body("The interview ID in the path does not match the ID in the request body.");
          }
          return service.updateInterviewById(id, interview, userId);
     }


     /**
      * @param id of the interview to be deleted
      * @return either that the interview was deleted, or an error explaining why not
      */
     @DeleteMapping("/{id}")
     public ResponseEntity<String> deleteInterview(@PathVariable UUID id) {
          try {
               UUID userId = getLoggedInUserId();
               return service.deleteInterviewById(id, userId);
          } catch (Exception e) {
               return internalServerError().body("Failed to delete interview: " + e.getMessage());
          }
     }

     /**
      * @return the uuid of the user currently logged in, from the JWT Bearer token they authenticated with
      */
     private UUID getLoggedInUserId() {
          Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
          if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
               String userId = jwt.getClaim("user_id");
               if (userId != null) {
                    return fromString(userId);
               }
          }
          throw new RuntimeException("No valid authentication found");
     }
}