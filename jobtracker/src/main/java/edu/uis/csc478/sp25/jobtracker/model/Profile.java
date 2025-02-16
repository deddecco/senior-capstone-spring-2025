package edu.uis.csc478.sp25.jobtracker.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class Profile {
     public UUID id;
     public String name;
     public String email;
     public String title;
     public String bio;
     public String location;
}
