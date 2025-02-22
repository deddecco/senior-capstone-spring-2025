package edu.uis.csc478.sp25.jobtracker.repository;

import edu.uis.csc478.sp25.jobtracker.model.Interview;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface InterviewRepository extends CrudRepository<Interview, UUID> {

     @Query("""
                 SELECT * FROM interview 
                 WHERE user_id = :userId 
                 AND format = :format 
                 AND round = :round 
                 AND date = CAST(:date AS DATE) 
                 AND time = CAST(:time AS TIME)
             """)
     List<Interview> findByFilters(
             @Param("userId") UUID userId,
             @Param("format") String format,
             @Param("round") String round,
             @Param("date") LocalDate date,
             @Param("time") LocalTime time
     );
}
