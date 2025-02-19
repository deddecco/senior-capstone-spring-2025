package edu.uis.csc478.sp25.jobtracker.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
@Builder
// the fields in a Job class correspond to the columns in an job table
public class Job {
     @Id
     public UUID id;
     public String title;
     public String level;
     public int minSalary;
     public int maxSalary;
     public String location;
}
