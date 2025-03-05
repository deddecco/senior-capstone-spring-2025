package edu.uis.csc478.sp25.jobtracker.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.util.UUID;

@Data
@Builder
public class Job {
     @Id
     public UUID id;
     public String title;
     public String level;
     public int minSalary;
     public int maxSalary;
     public String location;
     public String status;

     @Column("user_id")
     public UUID userId; // Use userId instead of user_id
}