package edu.uis.csc478.sp25.jobtracker.service;

import edu.uis.csc478.sp25.jobtracker.model.CurrentTimeResponse;
import edu.uis.csc478.sp25.jobtracker.model.TimeZoneRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CurrentTimeService {
     private static final Logger LOG = LoggerFactory.getLogger(CurrentTimeService.class);

     public CurrentTimeResponse getCurrentTime() {
          return CurrentTimeResponse.builder()
                  .time("11:04 AM")
                  .date("01/16/25")
                  .dow("Saturday")
                  .build();
     }

     public void setTimeZone(TimeZoneRequest timeZone) {
          LOG.debug("Setting time zone to {}", timeZone.getTimeZone());
     }
}
