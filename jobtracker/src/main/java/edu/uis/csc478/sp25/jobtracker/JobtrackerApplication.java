package edu.uis.csc478.sp25.jobtracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JobtrackerApplication {

     public static void main(String[] args) {
          // starts the Spring Boot application accepting calls to RESTful API
          SpringApplication.run(JobtrackerApplication.class, args);
     }

}
