package model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "Interview")
public class Interview {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

     @ManyToOne
     @JoinColumn(name = "seeker_id", nullable = false)
     private Seeker seeker;

     @Column(name = "company_id", nullable = false)
     private Long companyId;

     @Column(name = "format", nullable = false)
     private String format; // Zoom, In-person, etc.

     @Column(name = "round", nullable = false)
     private int round;

     @Column(name = "date", nullable = false)
     private LocalDate date;

     @Column(name = "time", nullable = false)
     private LocalTime time;

     @Column(name = "location")
     private String location;

     // ===== Constructors =====

     public Interview() {
     }

     public Interview(Seeker seeker,
                      Long companyId,
                      String format,
                      int round,
                      LocalDate date,
                      LocalTime time,
                      String location) {
          this.seeker = seeker;
          this.companyId = companyId;
          this.format = format;
          this.round = round;
          this.date = date;
          this.time = time;
          this.location = location;
     }

     // ===== Getters and Setters =====

     public Long getId() {
          return id;
     }

     public Seeker getSeeker() {
          return seeker;
     }

     public void setSeeker(Seeker seeker) {
          this.seeker = seeker;
     }

     public Long getCompanyId() {
          return companyId;
     }

     public void setCompanyId(Long companyId) {
          this.companyId = companyId;
     }

     public String getFormat() {
          return format;
     }

     public void setFormat(String format) {
          this.format = format;
     }

     public int getRound() {
          return round;
     }

     public void setRound(int round) {
          this.round = round;
     }

     public LocalDate getDate() {
          return date;
     }

     public void setDate(LocalDate date) {
          this.date = date;
     }

     public LocalTime getTime() {
          return time;
     }

     public void setTime(LocalTime time) {
          this.time = time;
     }

     public String getLocation() {
          return location;
     }

     public void setLocation(String location) {
          this.location = location;
     }
}
