package repositories;

import model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
     // Count applications where status is one of the included statuses
     int countBySeekerIdAndStatusIn(Long seekerId, List<String> includedStatuses);
}