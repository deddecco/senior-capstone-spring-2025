package edu.uis.csc478.sp25.jobtracker.repository;

import edu.uis.csc478.sp25.jobtracker.model.Profile;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

// Interface for CRUD operations-- calls to this get converted by Spring behind the scenes to SQL statements
public interface ProfileRepository extends CrudRepository<Profile, UUID> {
     // Find a profile by email
     boolean existsByEmail(String email);

     @Modifying
     @Query(value = "INSERT INTO profile (id, name, email, title, bio, location) " +
             "VALUES (:userId, :name, :email, :title, :bio, :location)")
     void insertProfile(
             @Param("userId") UUID userId,
             @Param("name") String name,
             @Param("email") String email,
             @Param("title") String title,
             @Param("bio") String bio,
             @Param("location") String location
     );
}