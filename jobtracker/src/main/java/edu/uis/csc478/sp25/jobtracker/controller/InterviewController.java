package edu.uis.csc478.sp25.jobtracker.controller;

import edu.uis.csc478.sp25.jobtracker.model.Interview;
import edu.uis.csc478.sp25.jobtracker.security.SecurityUtil;
import edu.uis.csc478.sp25.jobtracker.service.InterviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static edu.uis.csc478.sp25.jobtracker.security.SecurityUtil.*;
import static java.util.Map.of;
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
      * @return a response entity with either an OK code and a list of all interviews associated with a user, or noContent if there are none
      */
     @GetMapping
     public ResponseEntity<List<Interview>> getAllInterviews() {
          List<Interview> interviews = service.getAllInterviewsForUser();
          return !interviews.isEmpty() ? ok(interviews) : noContent().build();
     }

     /**
      * @param id to find an interview
      * @return that interview to be displayed by the frontend
      */
     @GetMapping("/{id}")
     public ResponseEntity<Object> getInterviewById(@PathVariable UUID id) {
          UUID userId = getLoggedInUserId();
          Interview interview = service.getInterviewById(id).getBody();
          if (interview != null) {
               if (interview.user_id.equals(userId) || hasRole("ADMIN")) {
                    return ok(interview);
               } else {
                    Map<String, Object> errorResponse = of(
                            "status", NOT_FOUND.value(),
                            "error", "Not Found",
                            "message", "An interview with ID " + id + " does not exist or you don't have access to it"
                    );
                    return new ResponseEntity<>(errorResponse, NOT_FOUND);
               }
          } else {
               Map<String, Object> errorResponse = of(
                       "status", NOT_FOUND.value(),
                       "error", "Not Found",
                       "message", "An interview with ID " + id + " does not exist"
               );
               return new ResponseEntity<>(errorResponse, NOT_FOUND);
          }
     }

     /**
      * @param format optional parameter for the search
      * @param round  optional parameter for the search
      * @param date   optional parameter for the search
      * @param time   optional parameter for the search
      * @return all the interviews that match all the parameters given in the query,
      * or all the user's interviews if none of the parameters are specified
      */
     @GetMapping("/search")
     public ResponseEntity<List<Interview>> searchInterviews(
             @RequestParam(required = false) String format,
             @RequestParam(required = false) String round,
             @RequestParam(required = false) LocalDate date,
             @RequestParam(required = false) LocalTime time) {
          List<Interview> matchingInterviews = service.searchInterviews(format, round, date, time);
          return !matchingInterviews.isEmpty() ? ok(matchingInterviews) : noContent().build();
     }

     /**
      * @param interview an interview
      * @return either that an interview was created, or an error explaining why not
      */
     @PostMapping
     public ResponseEntity<String> createInterview(@RequestBody Interview interview) {
          try {
               return service.createInterview(interview);
          } catch (Exception e) {
               return internalServerError().body("Failed to create interview: " + e.getMessage());
          }
     }

     /**
      * @param id        of an interview
      * @param interview replacement values for the fields of that interview-- do not change ids
      * @return either that the interview was updated, or an error explaining why not
      */
     @PutMapping("/{id}")
     @PreAuthorize("hasRole('ADMIN') or @interviewSecurity.checkOwner(#id)")
     public ResponseEntity<String> updateInterview(@PathVariable UUID id,
                                                   @RequestBody Interview interview) {
          if (!interview.id.equals(id)) {
               return badRequest().body("The interview ID in the path does not match the ID in the request body.");
          }
          return service.updateInterviewById(id, interview);
     }

     /**
      * @param id of the interview to be deleted
      * @return either that the interview was deleted, or an error explaining why not
      */
     @DeleteMapping("/{id}")
     @PreAuthorize("hasRole('ADMIN') or @interviewSecurity.checkOwner(#id)")
     public ResponseEntity<String> deleteInterview(@PathVariable UUID id) {
          try {
               return service.deleteInterviewById(id);
          } catch (Exception e) {
               return internalServerError().body("Failed to delete interview: " + e.getMessage());
          }
     }
}
