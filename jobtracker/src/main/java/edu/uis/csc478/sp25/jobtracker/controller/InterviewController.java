package edu.uis.csc478.sp25.jobtracker.controller;

import edu.uis.csc478.sp25.jobtracker.model.Interview;
import edu.uis.csc478.sp25.jobtracker.service.InterviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

     // GET /interviews
     @GetMapping
     public ResponseEntity<List<Interview>> getAllInterviews() {
          List<Interview> interviews = service.getAllInterviews();
          return !interviews.isEmpty() ? ok(interviews) : noContent().build();
     }

     // GET /interviews/{id}
     @GetMapping("/{id}")
     public ResponseEntity<Object> getInterviewById(@PathVariable UUID id) {
          ResponseEntity<Interview> interviewResponse = service.getInterviewById(id);
          if (interviewResponse.getBody() != null) {
               return ok(interviewResponse.getBody());
          }
          Map<String, Object> errorResponse = new HashMap<>();
          errorResponse.put("status", NOT_FOUND.value());
          errorResponse.put("error", "Not Found");
          errorResponse.put("message", "An interview with ID " + id + " does not exist");
          return new ResponseEntity<>(errorResponse, NOT_FOUND);
     }

     // GET /interviews/search
     @GetMapping("/search")
     public ResponseEntity<List<Interview>> searchInterviews(
             @RequestParam(required = false) UUID userId,
             @RequestParam(required = false) String format,
             @RequestParam(required = false) String round,
             @RequestParam(required = false) LocalDate date,
             @RequestParam(required = false) LocalTime time) {
          List<Interview> matchingInterviews = service.searchInterviews(userId, format, round, date, time);
          return !matchingInterviews.isEmpty() ? ok(matchingInterviews) : noContent().build();
     }

     /// / ADMIN /////

     // POST /interviews
     @PostMapping
     public ResponseEntity<String> createInterview(@RequestBody Interview interview) {
          try {
               if (service.existsByUUID(interview.getId())) {
                    return new ResponseEntity<>("Interview already exists.", CONFLICT);
               }
               service.createInterview(interview);
               return new ResponseEntity<>("Interview created successfully!", CREATED);
          } catch (Exception e) {
               return internalServerError().body("Failed to create interview: " + e.getMessage());
          }
     }

     // PUT /interviews/{id}
     @PutMapping("/{id}")
     public ResponseEntity<String> updateInterview(@PathVariable UUID id, @RequestBody Interview interview) {
          if (!interview.getId().equals(id)) {
               return badRequest().body("The interview ID in the path does not match the ID in the request body.");
          }
          return service.updateInterviewById(id, interview);
     }

     // DELETE /interviews/{id}
     @DeleteMapping("/{id}")
     public ResponseEntity<Void> deleteInterview(@PathVariable UUID id) {
          try {
               service.deleteInterviewById(id);
               return noContent().build();
          } catch (Exception e) {
               return status(INTERNAL_SERVER_ERROR).build();
          }
     }
}
