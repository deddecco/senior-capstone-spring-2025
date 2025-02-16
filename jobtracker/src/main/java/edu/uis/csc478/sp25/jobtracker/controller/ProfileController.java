package edu.uis.csc478.sp25.jobtracker.controller;

import edu.uis.csc478.sp25.jobtracker.model.Profile;
import edu.uis.csc478.sp25.jobtracker.service.ProfileService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class ProfileController {
     final ProfileService service;

     public ProfileController(ProfileService service) {
          this.service = service;
     }

     @GetMapping("current")
     Profile getCurrentProfile() {
          return service.getCurrentProfile();
     }

     @PostMapping("save")
     void saveProfile(@RequestBody Profile profile) {
          service.saveProfile(profile);
     }
}
