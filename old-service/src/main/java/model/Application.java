package model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "model.Application")
public class Application {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

     @ManyToOne
     @JoinColumn(name = "seeker_id", nullable = false)
     private Seeker seeker;

     @Column(name = "status", nullable = false)
     private String status;

     @Column(name = "submit_date", nullable = false)
     private LocalDate submitDate;

     @Column(name = "last_status_change_date")
     private LocalDate lastStatusChangeDate;

     // ===== Constructors =====

     public Application() {
     }

     public Application(Seeker seeker, String status, LocalDate submitDate, LocalDate lastStatusChangeDate) {
          this.seeker = seeker;
          this.status = status;
          this.submitDate = submitDate;
          this.lastStatusChangeDate = lastStatusChangeDate;
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

     public String getStatus() {
          return status;
     }

     public void setStatus(String status) {
          this.status = status;
     }

     public LocalDate getSubmitDate() {
          return submitDate;
     }

     public void setSubmitDate(LocalDate submitDate) {
          this.submitDate = submitDate;
     }

     public LocalDate getLastStatusChangeDate() {
          return lastStatusChangeDate;
     }

     public void setLastStatusChangeDate(LocalDate lastStatusChangeDate) {
          this.lastStatusChangeDate = lastStatusChangeDate;
     }
}
