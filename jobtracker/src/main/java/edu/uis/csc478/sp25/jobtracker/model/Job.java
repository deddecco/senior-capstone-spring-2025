package edu.uis.csc478.sp25.jobtracker.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
public class Job {
     @Id
     public UUID id;
     public String title;
     public String level;

     @Column("minsalary")
     public int minSalary;

     @Column("maxsalary")
     public int maxSalary;

     public String location;
     public String status;
     public String company;

     @Column("user_id")
     public UUID userId;

     @Column("last_modified")
     private OffsetDateTime lastModified;

     @Column("favorite")
     private boolean favorite;
}