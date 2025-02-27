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
// Interface for CRUD operations-- calls to this get converted by Spring behind the scenes to SQL statements
public interface InterviewRepository extends CrudRepository<Interview, UUID> {

     @Query("""
                 SELECT * FROM interview\s
                 WHERE user_id = :userId\s
                 AND format = :format\s
                 AND round = :round\s
                 AND date = CAST(:date AS DATE)\s
                 AND time = CAST(:time AS TIME)
            \s""")
     List<Interview> findByFilters(
             @Param("userId") UUID userId,
             @Param("format") String format,
             @Param("round") String round,
             @Param("date") LocalDate date,
             @Param("time") LocalTime time
     );
}
