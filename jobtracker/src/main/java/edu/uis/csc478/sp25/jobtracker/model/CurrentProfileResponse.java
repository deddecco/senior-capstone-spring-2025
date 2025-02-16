package edu.uis.csc478.sp25.jobtracker.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CurrentProfileResponse {
     public UUID id;
     public String name;
     public String email;
     public String currentTitle;
     public String bio;
     public String location;
}
