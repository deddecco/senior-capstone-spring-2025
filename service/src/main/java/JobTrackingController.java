import model.Seeker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import repositories.ApplicationRepository;
import repositories.SeekerRepository;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
public class JobTrackingController {

     @Autowired
     private SeekerRepository seekerRepository;

     @GetMapping("/profile")
     public SeekerProfile getProfile() {
          String email = getCurrentUserEmail();
          Seeker seeker = seekerRepository.findByEmail(email);

          if (seeker == null) {
               throw new RuntimeException("User not found");
          }

          // Return a DTO with pre-populated user data
          return new SeekerProfile(seeker.getName(), seeker.getEmail(), seeker.getCurrentTitle(),
                  seeker.getLocation(), seeker.getBio());
     }

     @PutMapping("/profile/update")
     public boolean updateProfile(@RequestParam String name,
                                  @RequestParam String email,
                                  @RequestParam String title,
                                  @RequestParam String location,
                                  @RequestParam String bio) {
          String currentEmail = getCurrentUserEmail();
          Seeker seeker = seekerRepository.findByEmail(currentEmail);

          if (seeker == null) {
               throw new RuntimeException("User not found");
          }

          // Update fields
          seeker.setName(name);
          seeker.setEmail(email);
          seeker.setCurrentTitle(title);
          seeker.setLocation(location);
          seeker.setBio(bio);

          // Save updated seeker to database
          seekerRepository.save(seeker);

          return true;
     }

     @Autowired
     private ApplicationRepository applicationRepository;

     @GetMapping("/count/applied")
     public int getCurrentAppsCount() {
          String email = getCurrentUserEmail();
          Seeker seeker = seekerRepository.findByEmail(email);

          if (seeker == null) {
               throw new RuntimeException("User not found");
          }

          // List of statuses to include in the count
          List<String> includedStatuses = List.of("Applied", "Screening", "model.Interview", "Rejected", "Offer", "Hired");

          return applicationRepository.countBySeekerIdAndStatusIn(seeker.getId(), includedStatuses);
     }

     @GetMapping("/count/interviews")
     public int getCurrentInterviewsCount() {
          // TODO: Implement logic to count jobs where status is "interview"
          return 0;
     }

     @GetMapping("/count/saved")
     public int getCurrentSavedCount() {
          // TODO: Implement logic to count jobs where status is "saved"
          return 0;
     }

     @GetMapping("/count/pipeline")
     public Map<String, Integer> getPipelineCount() {
          // TODO: Implement logic to return counts of jobs per status in the application pipeline
          return null;
     }

     @PostMapping("/save")
     public Long saveJob(@RequestParam String position,
                         @RequestParam String company,
                         @RequestParam String location,
                         @RequestParam Double minSalary,
                         @RequestParam Double maxSalary) {
          // TODO: Implement logic to save a new job with status "saved" and return jobID
          return null;
     }

     @DeleteMapping("/remove/{jobID}")
     public boolean removeJob(@PathVariable Long jobID) {
          // TODO: Implement logic to remove a job and return success/failure
          return false;
     }

     @PutMapping("/favorite/{jobID}")
     public boolean makeFavorite(@PathVariable Long jobID) {
          // TODO: Implement logic to mark a job as favorite and return success/failure
          return false;
     }

     @PutMapping("/unfavorite/{jobID}")
     public boolean undoFavorite(@PathVariable Long jobID) {
          // TODO: Implement logic to remove favorite flag from a job and return success/failure
          return false;
     }

     @PutMapping("/status/{jobID}")
     public boolean changeJobStatus(@PathVariable Long jobID,
                                    @RequestParam String newStatus) {
          // TODO: Implement logic to update the status of a job and return success/failure
          return false;
     }

     @GetMapping("/all")
     public List<Map<String, Object>> getAllJobs() {
          // TODO: Implement logic to return all jobs with relevant information
          return null;
     }

     @PostMapping("/interview/save")
     public void saveNewInterview(@RequestParam Long jobID,
                                  @RequestParam String date,
                                  @RequestParam String time,
                                  @RequestParam int interviewRound,
                                  @RequestParam String contactInfo) {
          // TODO: Implement logic to save an interview record associated with a job
     }

     @GetMapping("/interviews/upcoming")
     public List<Map<String, Object>> getUpcomingInterviews() {
          // TODO: Implement logic to return a list of upcoming interviews (within 2 weeks)
          return null;
     }

     @GetMapping("/activity/recent")
     public List<Map<String, Object>> getRecentActivity() {
          // TODO: Implement logic to return up to 5 most recent status changes for jobs
          return null;
     }

     private String getCurrentUserEmail() {
          Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
          if (principal instanceof UserDetails) {
               return ((UserDetails) principal).getUsername();
          }
          return principal.toString();
     }
}