package edu.uis.csc478.sp25.jobtracker.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
@Builder
// the fields in a Profile class correspond to the columns in a profile table
public class Profile {
     @Id
     public UUID user_id;
     public String name;
     public String email;
     public String title;
     public String bio;
     public String location;
}
