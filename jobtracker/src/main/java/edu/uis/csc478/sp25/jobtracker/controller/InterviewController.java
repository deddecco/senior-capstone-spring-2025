package edu.uis.csc478.sp25.jobtracker.controller;

import edu.uis.csc478.sp25.jobtracker.model.Interview;
import edu.uis.csc478.sp25.jobtracker.service.InterviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.Map.*;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.*;

@CrossOrigin
@RestController
@RequestMapping("/interviews")
public class InterviewController {

     private static final Logger logger = LoggerFactory.getLogger(InterviewController.class);
     private final InterviewService service;

     public InterviewController(InterviewService service) {
          this.service = service;
     }

     /**
      * Get all interviews for the currently logged-in user.
      * @return a response entity with either an OK code and a list of all interviews associated with a user, or noContent if there are none
      */
     @GetMapping
     public ResponseEntity<List<Interview>> getAllInterviews() {
          List<Interview> interviews = service.getAllInterviewsForUser();
          return interviews.isEmpty() ? noContent().build() : ok(interviews);
     }

     /**
      * Get a specific interview by ID.
      *
      * @param id the ID of the interview to retrieve
      * @return that interview to be displayed by the frontend
      */
     @GetMapping("/{id}")
     public ResponseEntity<Object> getInterviewById(@PathVariable UUID id) {
          try {
               Interview interview = service.getInterviewById(id);
               return ok(interview);
          } catch (RuntimeException e) {
               logger.error("Error fetching interview by ID {}", id, e);
               Map<String, Object> errorResponse = new HashMap<>();
               errorResponse.put("status", NOT_FOUND.value());
               errorResponse.put("error", "Not Found");
               errorResponse.put("message", "An interview with ID " + id + " does not exist or you don't have access to it");
               return new ResponseEntity<>(errorResponse, NOT_FOUND);
          }
     }

     /**
      * Search interviews based on optional parameters.
      *
      * @param format optional parameter for the search
      * @param round  optional parameter for the search
      * @param date   optional parameter for the search
      * @param time   optional parameter for the search
      * @return all the interviews that match all the parameters given in the query,
      * or all the user's interviews if none of the parameters are specified
      */
     /// todo test
     @GetMapping("/search")
     public ResponseEntity<List<Interview>> searchInterviews(
             @RequestParam(required = false) String format,
             @RequestParam(required = false) String round,
             @RequestParam(required = false) LocalDate date,
             @RequestParam(required = false) LocalTime time) {
          List<Interview> matchingInterviews = service.searchInterviews(format, round, date, time);
          return matchingInterviews.isEmpty() ? noContent().build() : ok(matchingInterviews);
     }

     /**
      * Create a new interview.
      *
      * @param interview an interview object containing its information
      * @return either that an interview was created, or an error explaining why not
      */
     @PostMapping
     public ResponseEntity<Object> createInterview(@RequestBody Interview interview) {
          try {
               // Validate input
               if (interview.getDate() == null || interview.getTime() == null) {
                    return badRequest().body(of("message", "Date and Time are required."));
               }

               // Create interview
               Interview createdInterview = service.createInterview(interview);
               return status(CREATED).body(createdInterview);
          } catch (RuntimeException e) {
               logger.error("Failed to create interview: {}", e.getMessage(), e);

               if (e.getMessage().contains("already exists")) {
                    return status(CONFLICT).body(of("message",
                            "Interview already exists."));
               }
               return status(INTERNAL_SERVER_ERROR)
                       .body(of("message", "Failed to create interview: " + e.getMessage()));
          }
     }

     /**
      * Update an existing interview.
      *
      * @param id        the ID of the interview to update
      * @param interview replacement values for the fields of that interview-- do not change IDs
      * @return either that the interview was updated, or an error explaining why not
      */
     @PutMapping("/{id}")
     public ResponseEntity<Object> updateInterview(@PathVariable UUID id, @RequestBody Interview interview) {
          if (interview.id == null || !interview.id.equals(id)) {
               return badRequest().body(of("message", "The interview ID in the path does not match the ID in the request body"));
          }

          try {
               Interview updatedInterview = service.updateInterviewById(id, interview);
               return ok(updatedInterview);
          } catch (RuntimeException e) {
               logger.error("Failed to update interview with ID {}", id, e);
               if (e.getMessage().contains("not found") || e.getMessage().contains("permission")) {
                    return status(NOT_FOUND).body(of("message", e.getMessage()));
               }
               return status(INTERNAL_SERVER_ERROR).body(of("message", "Failed to update interview: " + e.getMessage()));
          }
     }

     // fixme: failed to delete, bad SQL grammar
     /**
      * Delete an existing interview.
      *
      * @param id the ID of the interview to delete
      * @return either that the interview was deleted, or an error explaining why not
      */
     @DeleteMapping("/{id}")
     public ResponseEntity<Object> deleteInterview(@PathVariable UUID id) {
          try {
               service.deleteInterviewById(id);
               return ok(of("message", "Interview deleted successfully"));
          } catch (RuntimeException e) {
               logger.error("Failed to delete interview with ID {}", id, e);
               if (e.getMessage().contains("not found") || e.getMessage().contains("permission")) {
                    return status(NOT_FOUND).body(of("message", e.getMessage()));
               }
               return status(INTERNAL_SERVER_ERROR).body(of("message", "Failed to delete interview: " + e.getMessage()));
          }
     }
}