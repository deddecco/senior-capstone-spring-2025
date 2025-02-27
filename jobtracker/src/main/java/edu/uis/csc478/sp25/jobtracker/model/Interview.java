package edu.uis.csc478.sp25.jobtracker.model;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.sql.Date;
import java.sql.Time;
import java.util.UUID;

@Data
@Builder
// the fields in an Interview object correspond to the columns in an interview table
public class Interview {
     @Id
     public UUID id;
     public UUID user_id;
     public String format;
     public String round;
     public Date date;
     public Time time;
}
