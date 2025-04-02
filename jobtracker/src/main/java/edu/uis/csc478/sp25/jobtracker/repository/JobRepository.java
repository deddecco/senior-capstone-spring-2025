package edu.uis.csc478.sp25.jobtracker.repository;

import edu.uis.csc478.sp25.jobtracker.model.Job;
import org.springframework.data.jdbc.repository.query.Modifying;
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

     @Query("""
              SELECT j FROM Job j 
              WHERE j.user_id = :userId
                AND (:title IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :title, '%')))
                AND (:level IS NULL OR LOWER(j.level) = LOWER(:level))
                AND (:minSalary IS NULL OR j.minSalary >= :minSalary)
                AND (:maxSalary IS NULL OR j.maxSalary <= :maxSalary)
                AND (:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%')))
                AND (:status IS NULL OR LOWER(j.status) = LOWER(:status))
             """)
     List<Job> findJobsByFilters(
             @Param("user_id") UUID userId,
             @Param("title") String title,
             @Param("level") String level,
             @Param("minsalary") Integer minSalary,
             @Param("maxsalary") Integer maxSalary,
             @Param("location") String location,
             @Param("status") String status // Add status parameter
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
                 SELECT j.status, COUNT(j)
                 FROM Job j
                 WHERE j.user_id = :userId
                 GROUP BY j.status
             """)
     List<Object[]> countJobsByStatus(@Param("user_id") UUID userId);

     List<Job> findByUserId(UUID userId);

     @Modifying
     @Query("INSERT INTO job (id, user_id, title, level, minsalary, maxsalary, location, status) " +
             "VALUES (:#{#job.id}, :#{#job.userId}, :#{#job.title}, :#{#job.level}, :#{#job.minSalary}, " +
             ":#{#job.maxSalary}, :#{#job.location}, :#{#job.status})")
     void insertJob(@Param("job") Job job);

     @Modifying
     @Query("UPDATE job SET title = :#{#job.title}, level = :#{#job.level}, minsalary = :#{#job.minSalary}, " +
             "maxsalary = :#{#job.maxSalary}, location = :#{#job.location}, status = :#{#job.status} " +
             "WHERE id = :#{#job.id} AND user_id = :#{#job.userId}")
     int updateJob(@Param("job") Job job);
}