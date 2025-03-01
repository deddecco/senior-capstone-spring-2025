package edu.uis.csc478.sp25.jobtracker.repository;

import edu.uis.csc478.sp25.jobtracker.model.Interview;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InterviewRepository extends CrudRepository<Interview, UUID> {

     @Query("SELECT * FROM interview WHERE user_id = :userId")
     List<Interview> findByUserId(@Param("userId") UUID userId);

     @Query("SELECT * FROM interview WHERE id = :id AND user_id = :userId")
     Optional<Interview> findByIdAndUserId(@Param("id") UUID id, @Param("userId") UUID userId);

     @Query("""
             SELECT * FROM interview
             WHERE user_id = :userId
             AND (:format IS NULL OR format = :format)
             AND (:round IS NULL OR round = :round)
             AND (:date IS NULL OR date = CAST(:date AS DATE))
             AND (:time IS NULL OR time = CAST(:time AS TIME))
             """)
     List<Interview> findByFiltersAndUserId(
             @Param("userId") UUID userId,
             @Param("format") String format,
             @Param("round") String round,
             @Param("date") LocalDate date,
             @Param("time") LocalTime time
     );

     @Query("SELECT EXISTS(SELECT 1 FROM interview WHERE id = :id AND user_id = :userId)")
     boolean existsByIdAndUserId(@Param("id") UUID id, @Param("userId") UUID userId);
}
