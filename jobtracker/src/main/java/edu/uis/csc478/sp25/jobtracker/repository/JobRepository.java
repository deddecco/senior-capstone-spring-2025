package edu.uis.csc478.sp25.jobtracker.repository;

import edu.uis.csc478.sp25.jobtracker.model.Job;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for managing Job entities.
 */
public interface JobRepository extends CrudRepository<Job, UUID> {

     /**
      * Finds jobs based on the provided filters. All parameters are optional.
      *
      * @param userId    User ID to filter by.
      * @param title     Job title to search for.
      * @param level     Job level to filter by.
      * @param minSalary Minimum salary to filter by.
      * @param maxSalary Maximum salary to filter by.
      * @param location  Job location to search for.
      * @return List of jobs matching the filters.
      */
     @Query("""
             SELECT * FROM job 
             WHERE (:userId IS NULL OR user_id = :userId)
             AND (:title IS NULL OR LOWER(title) LIKE LOWER(CONCAT('%', :title, '%')))
             AND (:level IS NULL OR LOWER(level) = LOWER(:level))
             AND (:minSalary IS NULL OR minSalary >= :minSalary)
             AND (:maxSalary IS NULL OR maxSalary <= :maxSalary)
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

     /**
      * Retrieves all jobs associated with the given user ID.
      *
      * @param userId User ID to fetch jobs for.
      * @return List of jobs belonging to the user.
      */
     @Query("SELECT * FROM job WHERE user_id = :userId")
     List<Job> findAllByUserId(@Param("userId") UUID userId);

     /**
      * Finds a job by its ID and user ID.
      *
      * @param id     Job ID to search for.
      * @param userId User ID to verify ownership.
      * @return Optional containing the job if found, or an empty Optional otherwise.
      */
     @Query("SELECT * FROM job WHERE id = :id AND user_id = :userId")
     Optional<Job> findByIdAndUserId(@Param("id") UUID id, @Param("userId") UUID userId);

     /**
      * Checks if a job exists with the given ID and user ID.
      *
      * @param id     Job ID to check.
      * @param userId User ID to verify ownership.
      * @return True if the job exists for the user, false otherwise.
      */
     @Query("SELECT CASE WHEN EXISTS (SELECT 1 FROM job WHERE id = :id AND user_id = :userId) THEN TRUE ELSE FALSE END")
     boolean existsByIdAndUserId(@Param("id") UUID id, @Param("userId") UUID userId);

     /**
      * Retrieves the count of jobs by status for the given user ID.
      *
      * @param userId User ID to fetch job status counts for.
      * @return List of arrays containing the job status and its count.
      */
     @Query("""
             SELECT status, COUNT(*) as count
             FROM job
             WHERE user_id = :userId
             GROUP BY status
             """)
     List<Object[]> countJobsByStatus(@Param("userId") UUID userId);

     List<Job> findByUserId(UUID userId);
}