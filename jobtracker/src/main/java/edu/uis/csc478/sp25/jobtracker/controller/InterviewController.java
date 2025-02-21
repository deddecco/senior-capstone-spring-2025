package edu.uis.csc478.sp25.jobtracker.controller;

import edu.uis.csc478.sp25.jobtracker.model.Interview;
import edu.uis.csc478.sp25.jobtracker.service.InterviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/interviews")
public class InterviewController {

     private final InterviewService service;

     public InterviewController(InterviewService service) {
          this.service = service;
     }

     //// USER ////

     // GET /interviews
     @GetMapping
     public ResponseEntity<List<Interview>> getAllInterviews() {
          return null;
     }

     // GET /interviews/{id}
     @GetMapping("/{id}")
     public ResponseEntity<Object> getInterviewById(@PathVariable UUID id) {
          return null;
     }

     // GET /interviews/search
     @GetMapping("/search")
     public ResponseEntity<List<Interview>> searchInterviews(
             @RequestParam UUID user_id,
             @RequestParam String format,
             @RequestParam String round,
             @RequestParam String date,
             @RequestParam String time) {
          return null;
     }

     //// ADMIN ////

     // POST /interviews
     @PostMapping
     public ResponseEntity<String> createInterview(@RequestBody Interview interview) {
          return null;
     }

     // PUT /interviews/{id}
     @PutMapping("/{id}")
     public ResponseEntity<String> updateInterview(@PathVariable UUID id, @RequestBody Interview interview) {
          return null;
     }

     // DELETE /interviews/{id}
     @DeleteMapping("/{id}")
     public ResponseEntity<Void> deleteInterview(@PathVariable UUID id) {
          return null;
     }
}