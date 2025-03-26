package edu.uis.csc478.sp25.jobtracker.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table(schema = "public", name = "profile")
public class Profile {
     @Id
     public UUID id;

     @Column(value = "name")
     public String name;

     @Column(value = "email")
     public String email;

     @Column(value = "title")
     public String title;

     @Column(value = "bio")
     public String bio;

     @Column(value = "location")
     public String location;

     public UUID getId() {
          return id;
     }

     public void setId(UUID id) {
          this.id = id;
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

     public String getTitle() {
          return title;
     }

     public void setTitle(String title) {
          this.title = title;
     }

     public String getBio() {
          return bio;
     }

     public void setBio(String bio) {
          this.bio = bio;
     }

     public String getLocation() {
          return location;
     }

     public void setLocation(String location) {
          this.location = location;
     }

}