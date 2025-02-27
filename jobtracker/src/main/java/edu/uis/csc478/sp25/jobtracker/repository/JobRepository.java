package edu.uis.csc478.sp25.jobtracker.repository;

import edu.uis.csc478.sp25.jobtracker.model.Job;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;
// Interface for CRUD operations-- calls to this get converted by Spring behind the scenes to SQL statements
public interface JobRepository extends CrudRepository<Job, UUID> {

     @Query("SELECT * FROM job WHERE " +
             "(:title IS NULL OR title LIKE CONCAT('%', :title, '%')) AND " +
             "(:level IS NULL OR level = :level) AND " +
             "(:minSalary IS NULL OR min_salary >= :minSalary) AND " +
             "(:maxSalary IS NULL OR max_salary <= :maxSalary) AND " +
             "(:location IS NULL OR location LIKE CONCAT('%', :location, '%'))")
     List<Job> findByFilters(
             @Param("title") String title,
             @Param("level") String level,
             @Param("minSalary") Integer minSalary,
             @Param("maxSalary") Integer maxSalary,
             @Param("location") String location
     );
}
