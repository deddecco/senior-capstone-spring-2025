package edu.uis.csc478.sp25.jobtracker.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CurrentTimeResponse {
     private String time;
     private String date;
     private String dow;
}
