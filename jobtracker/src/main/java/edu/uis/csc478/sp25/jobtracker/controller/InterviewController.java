package edu.uis.csc478.sp25.jobtracker.controller;

import edu.uis.csc478.sp25.jobtracker.model.Interview;
import edu.uis.csc478.sp25.jobtracker.service.InterviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.*;

@CrossOrigin
@RestController
@RequestMapping("/interviews")
public class InterviewController {

     private final InterviewService service;

     public InterviewController(InterviewService service) {
          this.service = service;
     }

     /// / USER ////

     // GET /interviews/user/{userId}
     @GetMapping("/user/{userId}")
     public ResponseEntity<List<Interview>> getUserInterviews(@PathVariable UUID userId) {
          return service.getInterviewsByID(userId);
     }

     // GET /interviews/{id}
     @GetMapping("/{id}")
     public ResponseEntity<Object> getInterviewById(@PathVariable UUID id) {
          // This method needs to be implemented in the service layer
          // For now, we'll return a not implemented response
          return status(NOT_IMPLEMENTED).body("Method not implemented");
     }

     // GET /interviews/search
     @GetMapping("/search")
     public ResponseEntity<List<Interview>> searchInterviews(
             @RequestParam UUID user_id,
             @RequestParam(required = false) String format,
             @RequestParam(required = false) String round,
             @RequestParam(required = false) String date,
             @RequestParam(required = false) String time) {

          List<Interview> searchResults = service.searchInterviews(user_id,
                  format,
                  round,
                  date,
                  time);

          // if there are results, return them
          if (!searchResults.isEmpty()) {
               return ok(searchResults);
          }
          // otherwise return 204-- the request was able to make it through, but there was nothing to send back
          else {
               return noContent().build();
          }
     }

     /// / ADMIN ////

     // GET /interviews/all (Admin only)
     @GetMapping("/all")
     public ResponseEntity<List<Interview>> getAllInterviews() {
          List<Interview> interviews = service.getAllInterviews();
          return !interviews.isEmpty() ? ok(interviews) : noContent().build();
     }

     // POST /interviews
     @PostMapping
     public ResponseEntity<String> createInterview(@RequestBody Interview interview) {
          try {
               // Check if the interview already exists
               if (service.existsByUUID(interview.id)) {
                    return new ResponseEntity<>("Interview already exists.", CONFLICT);
               }
               service.createInterview(interview);
               // Use CREATED status for creation
               return new ResponseEntity<>("Interview created successfully!", CREATED);
          } catch (Exception e) {
               return internalServerError().body("Failed to create interview: " + e.getMessage());
          }
     }

     // PUT /interviews/{id}
     @PutMapping("/{id}")
     public ResponseEntity<String> updateInterview(@PathVariable UUID id,
                                                   @RequestBody Interview interview) {
          // Check if the interview id in the path matches the id in the request body
          if (!interview.getId().equals(id)) {
               return badRequest().body("The interview ID in the path does not match " +
                       "the ID in the request body.");
          }

          return service.updateInterviewById(id, interview);
     }

     // DELETE /interviews/{id}
     @DeleteMapping("/{id}")
     public ResponseEntity<Void> deleteInterview(@PathVariable UUID id) {
          if (!service.existsByUUID(id)) {
               return notFound().build();
          }

          ResponseEntity<String> deleteResult = service.deleteInterviewById(id);

          if (deleteResult.getStatusCode().is2xxSuccessful()) {
               return noContent().build();
          } else {
               return status(deleteResult.getStatusCode()).build();
          }
     }

}