package repositories;

import model.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {

     // Count interviews for the current user
     int countBySeekerId(Long seekerId);
}
