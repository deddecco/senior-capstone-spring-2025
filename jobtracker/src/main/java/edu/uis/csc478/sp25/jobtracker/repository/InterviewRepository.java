package edu.uis.csc478.sp25.jobtracker.repository;

import edu.uis.csc478.sp25.jobtracker.model.Interview;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
              AND (:format IS NULL OR LOWER(format) LIKE LOWER(CONCAT('%', :format, '%')))
              AND (:round IS NULL OR LOWER(round) LIKE LOWER(CONCAT('%', :round, '%')))
              AND (:date IS NULL OR date = :date)
              AND (:time IS NULL OR time = :time)
              AND (:company IS NULL OR LOWER(company) LIKE LOWER(CONCAT('%', :company, '%')))
             """)
     List<Interview> findByFiltersAndUserId(
             @Param("userId") UUID userId,
             @Param("format") String format,
             @Param("round") String round,
             @Param("date") String date,
             @Param("time") String time,
             @Param("company") String company  // New parameter
     );

     @Query("SELECT EXISTS(SELECT 1 FROM interview WHERE id = :id AND user_id = :userId)")
     boolean existsByIdAndUserId(@Param("id") UUID id, @Param("userId") UUID userId);

     @Modifying
     @Query("DELETE FROM interview WHERE id = :id AND user_id = :userId")
     int deleteByIdAndUserId(@Param("id") UUID id, @Param("userId") UUID userId);

     @Query("""
             SELECT * FROM interview 
             WHERE user_id = :userId 
             AND date > TO_CHAR(CURRENT_DATE, 'yyyy-MM-dd')
             """)
     List<Interview> findUpcomingByUserId(@Param("userId") UUID userId);
}