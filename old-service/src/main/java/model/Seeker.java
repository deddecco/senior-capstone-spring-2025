package model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "Seekers")

//class represents a job-seeker's profile in the database
public class Seeker {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;  // Auto-incremented primary key

     @Column(name = "user_id", nullable = false, unique = true)
     private UUID userId;  // Matches the Supabase auth UID

     @Column(name = "name", nullable = false)
     private String name;

     @Column(name = "email", nullable = false, unique = true)
     private String email;

     // title is optional
     @Column(name = "current_title", nullable = true)
     private String currentTitle;

     // bio is optional
     @Column(name = "bio", nullable = true)
     private String bio;

     // location is optional
     @Column(name = "location", nullable = true)
     private String location;

     // ===== Constructors =====

     public Seeker() {
     }

     public Seeker(UUID userId,
                   String name,
                   String email,
                   String currentTitle,
                   String bio,
                   String location) {
          this.userId = userId;
          this.name = name;
          this.email = email;
          this.currentTitle = currentTitle;
          this.bio = bio;
          this.location = location;
     }

     // ===== Getters and Setters =====

     public Long getId() {
          return id;
     }

     public UUID getUserId() {
          return userId;
     }

     public void setUserId(UUID userId) {
          this.userId = userId;
     }

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

     public String getCurrentTitle() {
          return currentTitle;
     }

     public void setCurrentTitle(String currentTitle) {
          this.currentTitle = currentTitle;
     }

     public String getBio() {
          return bio;
     }

     public void setBio(String bioTagline) {
          this.bio = bioTagline;
     }

     public String getLocation() {
          return location;
     }

     public void setLocation(String location) {
          this.location = location;
     }
}
