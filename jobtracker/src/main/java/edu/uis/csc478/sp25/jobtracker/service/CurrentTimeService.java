package edu.uis.csc478.sp25.jobtracker.service;

import edu.uis.csc478.sp25.jobtracker.model.CurrentTimeResponse;
import edu.uis.csc478.sp25.jobtracker.model.TimeZoneRequest;
import org.springframework.stereotype.Service;

@Service
public class CurrentTimeService {
     public CurrentTimeResponse getCurrentTime() {
          return CurrentTimeResponse.builder()
                  .time("11:04 AM")
                  .date("02/15/25")
                  .dow("Saturday")
                  .build();
     }

     public void setTimeZone(TimeZoneRequest timeZone) {
          System.out.println("Setting time zone to " + timeZone.getTimeZone());
     }
}
