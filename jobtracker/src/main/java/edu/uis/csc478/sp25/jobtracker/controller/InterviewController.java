package edu.uis.csc478.sp25.jobtracker.controller;

import edu.uis.csc478.sp25.jobtracker.model.Interview;
import edu.uis.csc478.sp25.jobtracker.service.InterviewService;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.Map.of;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.*;

/**
 * REST controller for managing Interview resources.
 * Handles CRUD operations and search for interviews.
 */
@CrossOrigin
@RestController
@RequestMapping("/interviews")
public class InterviewController {

     private static final Logger logger = getLogger(InterviewController.class);
     private final InterviewService service;

     /**
      * Constructs a new InterviewController with the given InterviewService.
      * @param service the InterviewService used for business logic
      */
     public InterviewController(InterviewService service) {
          this.service = service;
     }

     /**
      * Retrieves all interviews for the currently logged-in user.
      * @return 200 OK with the list of interviews, or 204 No Content if none found
      */
     @GetMapping
     public ResponseEntity<List<Interview>> getAllInterviews() {
          List<Interview> interviews = service.getAllInterviewsForUser();
          // If the list is empty, return 204 No Content;
          // else, return 200 OK with the list.
          return interviews.isEmpty() ? noContent().build() : ok(interviews);
     }

     /**
      * Retrieves a specific interview by its ID for the current user.
      * @param id the UUID of the interview
      * @return 200 OK with the interview, 404 if not found or no permission, or 500 on error
      */
     @GetMapping("/{id}")
     public ResponseEntity<Object> getInterviewById(@PathVariable UUID id) {
          try {
               Interview interview = service.getInterviewById(id);
               return ok(interview);
          } catch (RuntimeException e) {
               logger.error("Error fetching interview by ID {}", id, e);
               // Build a detailed error response for the frontend
               Map<String, Object> errorResponse = new HashMap<>();
               errorResponse.put("status", NOT_FOUND.value());
               errorResponse.put("error", "Not Found");
               errorResponse.put("message", "An interview with ID " + id + " does not exist or you don't have access to it");
               return new ResponseEntity<>(errorResponse, NOT_FOUND);
          }
     }

     /**
      * Searches interviews for the current user based on optional filters.
      * @param format  optional interview format filter (e.g., "phone", "onsite")
      * @param round   optional interview round filter (e.g., "first", "final")
      * @param date    optional interview date filter (as a string)
      * @param time    optional interview time filter (as a string)
      * @param company optional company filter
      * @return 200 OK with matching interviews, 204 No Content if none, or 500 on error
      */
     @GetMapping({"/search", "/search/"})
     public ResponseEntity<List<Interview>> searchInterviews(
             @RequestParam(required = false) String format,
             @RequestParam(required = false) String round,
             @RequestParam(required = false) String date,
             @RequestParam(required = false) String time,
             @RequestParam(required = false) String company) {
          try {
               logger.info("Received search request with filters: format={}, round={}, date={}, time={}, company={}",
                       format,
                       round,
                       date,
                       time,
                       company);

               // Ternary operators: If the parameter is not null, trim it; else, leave it null.
               String normalizedFormat = (format != null) ? format.trim() : null;
               String normalizedRound = (round != null) ? round.trim() : null;
               String normalizedDate = (date != null) ? date.trim() : null;
               String normalizedTime = (time != null) ? time.trim() : null;
               String normalizedCompany = (company != null) ? company.trim() : null;

               // Call the service layer to perform the search with normalized parameters
               List<Interview> matchingInterviews = service.searchInterviews(
                       normalizedFormat,
                       normalizedRound,
                       normalizedDate,
                       normalizedTime,
                       normalizedCompany
               );

               // Ternary operator: If no interviews match, return 204 No Content; else, return 200 OK with interviews.
               return matchingInterviews.isEmpty()
                       ? noContent().build()
                       : ok(matchingInterviews);
          } catch (Exception e) {
               logger.error("Error searching interviews", e);
               return status(INTERNAL_SERVER_ERROR).build();
          }
     }


     /**
      * Creates a new interview for the current user.
      * @param interview the Interview object containing its information
      * @return 201 Created with the new interview, 400 if required fields are missing, 409 if duplicate, or 500 on error
      */
     @PostMapping
     public ResponseEntity<Object> createInterview(@RequestBody Interview interview) {
          try {
               // Validate required fields before proceeding
               if (interview.getDate() == null || interview.getTime() == null) {
                    return badRequest().body(of("message", "Date and Time are required."));
               }

               // Create the interview using the service layer
               Interview createdInterview = service.createInterview(interview);
               return status(CREATED).body(createdInterview);
          } catch (RuntimeException e) {
               logger.error("Failed to create interview: {}", e.getMessage(), e);

               // If the exception message contains "already exists", return 409 Conflict; else, 500 Internal Server Error.
               if (e.getMessage().contains("already exists")) {
                    return status(CONFLICT).body(of("message", "Interview already exists."));
               }
               return status(INTERNAL_SERVER_ERROR)
                       .body(of("message", "Failed to create interview: " + e.getMessage()));
          }
     }

     /**
      * Updates an existing interview for the current user.
      * @param id        the UUID of the interview to update
      * @param interview the Interview object with updated information (IDs must match)
      * @return 200 OK with the updated interview, 400 if IDs mismatch, 404 if not found/no permission, or 500 on error
      */
     @PutMapping("/{id}")
     public ResponseEntity<Object> updateInterview(@PathVariable UUID id,
                                                   @RequestBody Interview interview) {
          // Validate that the interview ID in the path matches the ID in the body
          if (interview.id == null || !interview.id.equals(id)) {
               return badRequest().body(of("message",
                       "The interview ID in the path does not match the ID in the request body"));
          }

          try {
               Interview updatedInterview = service.updateInterviewById(id, interview);
               return ok(updatedInterview);
          } catch (RuntimeException e) {
               logger.error("Failed to update interview with ID {}", id, e);
               // If the exception message contains "not found" or "permission", return 404 Not Found; else, 500 Internal Server Error.
               if (e.getMessage().contains("not found") || e.getMessage().contains("permission")) {
                    return status(NOT_FOUND).body(of("message", e.getMessage()));
               }
               return status(INTERNAL_SERVER_ERROR)
                       .body(of("message", "Failed to update interview: " + e.getMessage()));
          }
     }

     /**
      * Deletes an existing interview for the current user.
      * @param id the UUID of the interview to delete
      * @return 204 No Content if deleted, 404 if not found/no permission, or 500 on error
      */
     @DeleteMapping("/{id}")
     public ResponseEntity<Object> deleteInterview(@PathVariable UUID id) {
          try {
               service.deleteInterviewById(id);
               // No content is returned on successful deletion
               return noContent().build();
          } catch (RuntimeException e) {
               logger.error("Failed to delete interview with ID {}", id, e);
               // If the exception message contains "not found" or "permission", return 404 Not Found; else, 500 Internal Server Error.
               if (e.getMessage().contains("not found") || e.getMessage().contains("permission")) {
                    return status(NOT_FOUND).body(of("message", e.getMessage()));
               }
               return status(INTERNAL_SERVER_ERROR)
                       .body(of("message", "Failed to delete interview: " + e.getMessage()));
          }
     }
}