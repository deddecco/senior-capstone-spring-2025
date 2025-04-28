package edu.uis.csc478.sp25.jobtracker.service;

import edu.uis.csc478.sp25.jobtracker.model.Interview;
import edu.uis.csc478.sp25.jobtracker.repository.InterviewRepository;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static edu.uis.csc478.sp25.jobtracker.security.SecurityUtil.getLoggedInUserId;
import static java.time.LocalDate.now;
import static java.time.LocalDate.parse;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Service layer for managing Interview entities.
 * Handles business logic for CRUD operations and search/filtering of interviews,
 * ensuring all operations are scoped to the currently logged-in user.
 */
@Service
public class InterviewService {

     private final InterviewRepository repository;
     private static final Logger logger = getLogger(InterviewService.class);

     /**
      * Constructs an InterviewService with the given InterviewRepository.
      * @param repository the InterviewRepository used for data access
      */
     public InterviewService(InterviewRepository repository) {
          this.repository = repository;
     }

     /**
      * Retrieves all interviews for the currently logged-in user.
      * @return a list of Interview objects belonging to the user (may be empty)
      */
     public List<Interview> getAllInterviewsForUser() {
          // Get the current user's UUID from security context
          UUID userId = getLoggedInUserId();
          // Retrieve all interviews for this user from the repository
          return repository.findByUserId(userId);
     }

     /**
      * Retrieves a specific interview by its ID for the current user.
      * Throws an exception if not found or not owned by the user.
      * @param id the UUID of the interview to retrieve
      * @return the Interview object if found and owned by the user
      * @throws RuntimeException if the interview does not exist
      * or does not belong to the user
      */
     public Interview getInterviewById(UUID id) {
          UUID userId = getLoggedInUserId();
          // Look up the interview by both ID and user ID to enforce ownership
          return repository.findByIdAndUserId(id, userId).orElseThrow(() -> new RuntimeException("Interview not found or you don't have permission to access it."));
     }

     /**
      * Searches interviews for the current user, using optional filter parameters.
      * Any parameter may be null, in which case it is ignored in the search.
      * @param format  optional interview format filter (e.g., "phone", "onsite")
      * @param round   optional round filter (e.g., "first", "final")
      * @param date    optional date filter (as a string)
      * @param time    optional time filter (as a string)
      * @param company optional company filter
      * @return a list of Interview objects matching the filters (may be empty)
      * @throws RuntimeException if an error occurs during search
      */
     public List<Interview> searchInterviews(String format, String round, String date, String time, String company) {
          try {
               UUID userId = getLoggedInUserId();
               // Delegate to repository, which handles nulls as
               // "don't filter on this field"
               return repository.findByFiltersAndUserId(userId, format, round, date, time, company);
          } catch (Exception e) {
               logger.error("Error searching interviews for user", e);
               throw new RuntimeException("Failed to search interviews", e);
          }
     }

     /**
      * Creates a new interview for the current user.
      * Enforces uniqueness by ID for this user.
      * @param interview the Interview object to create
      * @return the saved Interview object
      * @throws RuntimeException if an interview with the same ID already exists for the user
      */
     public Interview createInterview(Interview interview) {
          UUID userId = getLoggedInUserId();

          // Check if an interview with the same ID already exists for this user
          if (repository.existsByIdAndUserId(interview.getId(), userId)) {
               throw new RuntimeException("Interview already exists.");
          }

          // Set the user ID to ensure ownership
          interview.setUser_id(userId);

          // Save the interview to the repository
          return repository.save(interview);
     }

     /**
      * Updates an existing interview for the current user.
      * Only allows update if the interview exists and is owned by the user.
      * @param id        the UUID of the interview to update
      * @param interview the Interview object with updated data (must have same ID)
      * @return the updated Interview object
      * @throws RuntimeException if the interview does not exist, is not owned by the user, or ID mismatch
      */
     public Interview updateInterviewById(UUID id, Interview interview) {
          UUID userId = getLoggedInUserId();

          // Check that the interview exists and is owned by the user
          if (!repository.existsByIdAndUserId(id, userId)) {
               throw new RuntimeException("Interview not found or you don't have permission to update it.");
          }
          // Ensure the ID in the body matches the path
          if (!interview.id.equals(id)) {
               throw new RuntimeException("ID mismatch between path and request body.");
          }
          // Set the user ID to ensure ownership is not changed
          interview.user_id = userId;

          // Save the updated interview
          return repository.save(interview);
     }

     /**
      * Deletes an existing interview for the current user.
      * Only deletes if the interview exists and is owned by the user.
      * @param id the UUID of the interview to delete
      * @throws RuntimeException if the interview does not exist or is not owned by the user
      */
     public void deleteInterviewById(UUID id) {
          UUID userId = getLoggedInUserId();
          // Attempt to delete; repository returns number of rows deleted
          int deletedRows = repository.deleteByIdAndUserId(id, userId);
          if (deletedRows == 0) {
               throw new RuntimeException("Interview not found or you don't have permission to delete it.");
          }
     }

     /**
      * Checks if an interview exists by its UUID (not scoped to user).
      * @param id the UUID of the interview
      * @return true if an interview with this UUID exists, false otherwise
      */
     public boolean existsByUUID(UUID id) {
          return repository.existsById(id);
     }

     public List<Interview> getUpcomingInterviewsForUser() {
          LocalDate today = now();
          List<Interview> all = getAllInterviewsForUser();
          List<Interview> upcoming = new ArrayList<>();

          for (Interview interview : all) {
               if (interview.getDate() != null) {
                    try {
                         LocalDate interviewDate = parse(interview.getDate(), ISO_LOCAL_DATE);
                         if (interviewDate.isAfter(today)) {
                              upcoming.add(interview);
                         }
                    } catch (DateTimeParseException e) {
                         logger.warn("Invalid date format for interview {}", interview.getId());
                    }
               }
          }
          return upcoming;
     }

     /**
      * Returns the count of upcoming interviews (date after today) for the current user.
      * @return count >= 0
      */
     public int getCountOfUpcomingInterviews() {
          return getUpcomingInterviewsForUser().size();
     }
}