package edu.uis.csc478.sp25.jobtracker.repository;

import edu.uis.csc478.sp25.jobtracker.model.Interview;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

// Interface for CRUD operations-- calls to this get converted by Spring behind the scenes to SQL statements
public interface InterviewRepository extends CrudRepository<Interview, UUID> {
     List<Interview> findByUserId(UUID user_Id);

     List<Interview> findByFormatAndRound(String format,
                                          String round);

     List<Interview> findByDate(Date date);

     List<Interview> findByFilters(UUID userId,
                                   String format,
                                   String round,
                                   String date,
                                   String time);
}