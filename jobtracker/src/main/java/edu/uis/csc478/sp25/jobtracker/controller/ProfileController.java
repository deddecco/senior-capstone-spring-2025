package edu.uis.csc478.sp25.jobtracker.controller;

import edu.uis.csc478.sp25.jobtracker.model.CurrentProfileResponse;
import edu.uis.csc478.sp25.jobtracker.service.UserProfileService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class ProfileController {
     final UserProfileService service;

     public ProfileController(UserProfileService service) {
          this.service = service;
     }

     @GetMapping("current")
     CurrentProfileResponse getCurrentProfile() {
          return service.getCurrentProfile();
     }

     /*@PostMapping("my-timezone")
     void setTimeZone(@RequestBody TimeZoneRequest timeZone) {
          service.setTimeZone(timeZone);
     }*/
}
