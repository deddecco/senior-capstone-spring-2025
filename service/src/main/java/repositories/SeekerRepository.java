package repositories;

import model.Seeker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


// to interact with Supabase-- find individual profiles of seekers either by
// their emails or by their unique user ids
@Repository
public interface SeekerRepository extends JpaRepository<Seeker, Long> {

     // Find a seeker by their email (returns null if not found)
     Seeker findByEmail(String email);

     // Find a seeker by their Supabase user ID (returns null if not found)
     Seeker findByUserId(UUID userId);
}
