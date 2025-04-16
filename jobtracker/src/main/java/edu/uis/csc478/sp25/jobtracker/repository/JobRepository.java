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
                 SELECT * FROM job 
                 WHERE user_id = :userId
                 AND (:minSalary IS NULL OR minsalary >= :minSalary)
                 AND (:maxSalary IS NULL OR maxsalary <= :maxSalary)
                 AND (:title IS NULL OR LOWER(title) LIKE LOWER(CONCAT('%', :title, '%')))
                 AND (:level IS NULL OR LOWER(level) = LOWER(:level))
                 AND (:location IS NULL OR LOWER(location) LIKE LOWER(CONCAT('%', :location, '%')))
                 AND (:status IS NULL OR LOWER(status) = LOWER(:status))
                 AND (:company IS NULL OR LOWER(company) LIKE LOWER(CONCAT('%', :company, '%')))
             """)
     List<Job> findJobsByFilters(
             @Param("userId") UUID userId,
             @Param("title") String title,
             @Param("level") String level,
             @Param("minSalary") Integer minSalary,
             @Param("maxSalary") Integer maxSalary,
             @Param("location") String location,
             @Param("status") String status,
             @Param("company") String company
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
                 FROM job j
                 WHERE j.user_id = :userId
                 GROUP BY j.status
             """)
     List<Object[]> countJobsByStatus(@Param("userId") UUID userId);

     List<Job> findByUserId(UUID userId);

     @Modifying
     @Query("INSERT INTO job (id, user_id, title, level, minsalary, maxsalary, location, status, company) " +
             "VALUES (:#{#job.id}, :#{#job.userId}, :#{#job.title}, :#{#job.level}, :#{#job.minSalary}, " +
             ":#{#job.maxSalary}, :#{#job.location}, :#{#job.status}, :#{#job.company})")
     void insertJob(@Param("job") Job job);

     @Modifying
     @Query("UPDATE job SET title = :#{#job.title}, level = :#{#job.level}, minsalary = :#{#job.minSalary}, " +
             "maxsalary = :#{#job.maxSalary}, location = :#{#job.location}, status = :#{#job.status}, company = :#{#job.company} " +
             "WHERE id = :#{#job.id} AND user_id = :#{#job.userId}")
     int updateJob(@Param("job") Job job);

     @Query("SELECT * FROM job WHERE user_id = :userId AND status = 'Saved'")
     List<Job> findByUserIdAndStatus(@Param("userId") UUID userId);

     // fetch jobs ordered by last_modified descending
     @Query("SELECT * FROM job WHERE user_id = :userId ORDER BY last_modified DESC")
     List<Job> findAllByUserIdOrderByLastModifiedDesc(@Param("userId") UUID userId);
}