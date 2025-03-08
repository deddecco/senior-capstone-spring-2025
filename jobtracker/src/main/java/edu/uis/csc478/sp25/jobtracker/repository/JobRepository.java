package edu.uis.csc478.sp25.jobtracker.repository;

import edu.uis.csc478.sp25.jobtracker.model.Job;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JobRepository extends CrudRepository<Job, UUID> {

     @Query("""
            SELECT * FROM job 
            WHERE (:userId IS NULL OR user_id = :userId)
            AND (:title IS NULL OR LOWER(title) LIKE LOWER(CONCAT('%', :title, '%')))
            AND (:level IS NULL OR LOWER(level) = LOWER(:level))
            AND (:minSalary IS NULL OR minsalary >= :minSalary)
            AND (:maxSalary IS NULL OR maxsalary <= :maxSalary)
            AND (:location IS NULL OR LOWER(location) LIKE LOWER(CONCAT('%', :location, '%')))
            """)
     List<Job> findByFilters(
             @Param("userId") UUID userId,
             @Param("title") String title,
             @Param("level") String level,
             @Param("minSalary") Integer minSalary,
             @Param("maxSalary") Integer maxSalary,
             @Param("location") String location
     );

     @Query("SELECT * FROM job WHERE user_id = :userId")
     List<Job> findAllByUserId(@Param("userId") UUID userId);

     @Query("SELECT * FROM job WHERE id = :id AND user_id = :userId")
     Optional<Job> findByIdAndUserId(@Param("id") UUID id, @Param("userId") UUID userId);

     @Query("SELECT CASE WHEN EXISTS (SELECT 1 FROM job WHERE id = :id AND user_id = :userId) THEN TRUE ELSE FALSE END")
     boolean existsByIdAndUserId(@Param("id") UUID id, @Param("userId") UUID userId);

     @Query("""
            SELECT status, COUNT(*) as count
            FROM job
            WHERE user_id = :userId
            GROUP BY status
            """)
     List<Object[]> countJobsByStatus(@Param("userId") UUID userId);
}
