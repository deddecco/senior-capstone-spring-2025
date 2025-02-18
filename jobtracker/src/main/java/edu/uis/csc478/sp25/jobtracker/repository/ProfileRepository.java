package edu.uis.csc478.sp25.jobtracker.repository;

import edu.uis.csc478.sp25.jobtracker.model.Profile;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

// Interface for CRUD operations-- calls to this get converted by Spring behind the scenes to SQL statements
public interface ProfileRepository extends CrudRepository<Profile, UUID> {
     // Find a profile by email
     boolean existsByEmail(String email);
}