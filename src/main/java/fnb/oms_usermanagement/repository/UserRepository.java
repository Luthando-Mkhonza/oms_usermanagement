package fnb.oms_usermanagement.repository;

import fnb.oms_usermanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Spring Data JPA will automatically write the SQL query to find a user by their email
    Optional<User> findByEmail(String email);
    
    // A quick way to check if an email is already registered (useful for the /register endpoint)
    boolean existsByEmail(String email);
}
