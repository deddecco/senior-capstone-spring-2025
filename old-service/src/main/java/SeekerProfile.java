// establishing what a job seerk's profile looks like,
// creating a constructor, and setting up getters and setters

public class SeekerProfile {
     private String name;
     private String email;
     private String title;
     private String location;
     private String bio;

     public SeekerProfile(String name,
                          String email,
                          String title,
                          String location,
                          String bio) {
          this.name = name;
          this.email = email;
          this.title = title;
          this.location = location;
          this.bio = bio;
     }

     // Getters and Setters
     public String getName() {
          return name;
     }

     public void setName(String name) {
          this.name = name;
     }

     public String getEmail() {
          return email;
     }

     public void setEmail(String email) {
          this.email = email;
     }

     public String getTitle() {
          return title;
     }

     public void setTitle(String title) {
          this.title = title;
     }

     public String getLocation() {
          return location;
     }

     public void setLocation(String location) {
          this.location = location;
     }

     public String getBio() {
          return bio;
     }

     public void setBio(String bio) {
          this.bio = bio;
     }
}
