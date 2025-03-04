package edu.uis.csc478.sp25.jobtracker.controller;

import edu.uis.csc478.sp25.jobtracker.model.CurrentTimeResponse;
import edu.uis.csc478.sp25.jobtracker.model.TimeZoneRequest;
import edu.uis.csc478.sp25.jobtracker.service.CurrentTimeService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/time")
@CrossOrigin
public class CurrentTimeController {
     final CurrentTimeService service;

     public CurrentTimeController(CurrentTimeService service) {
          this.service = service;
     }

     @GetMapping("current")
     CurrentTimeResponse getCurrentTime(@AuthenticationPrincipal Jwt jwt) {
          System.out.println("user_id of the user is: " + jwt.getClaim("user_id"));
          return service.getCurrentTime();
     }

     @PostMapping("my-timezone")
     void setTimeZone(@RequestBody TimeZoneRequest timeZone) {
          service.setTimeZone(timeZone);
     }
}
