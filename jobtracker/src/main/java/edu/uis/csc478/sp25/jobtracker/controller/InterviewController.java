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

     @GetMapping
     public ResponseEntity<List<Interview>> getAllInterviews() {
          UUID userId = getLoggedInUserId();
          List<Interview> interviews = service.getAllInterviewsForUser(userId);
          return !interviews.isEmpty() ? ok(interviews) : noContent().build();
     }

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

     @PostMapping
     public ResponseEntity<String> createInterview(@RequestBody Interview interview) {
          try {
               UUID userId = getLoggedInUserId();
               return service.createInterview(interview, userId);
          } catch (Exception e) {
               return internalServerError().body("Failed to create interview: " + e.getMessage());
          }
     }

     @PutMapping("/{id}")
     public ResponseEntity<String> updateInterview(@PathVariable UUID id,
                                                   @RequestBody Interview interview) {
          UUID userId = getLoggedInUserId();
          if (!interview.id.equals(id)) {
               return badRequest().body("The interview ID in the path does not match the ID in the request body.");
          }
          return service.updateInterviewById(id, interview, userId);
     }


     @DeleteMapping("/{id}")
     public ResponseEntity<String> deleteInterview(@PathVariable UUID id) {
          try {
               UUID userId = getLoggedInUserId();
               return service.deleteInterviewById(id, userId);
          } catch (Exception e) {
               return internalServerError().body("Failed to delete interview: " + e.getMessage());
          }
     }

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
