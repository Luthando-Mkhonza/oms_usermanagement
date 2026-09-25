package fnb.oms_usermanagement.repository;

import fnb.oms_usermanagement.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCredentialRepository extends JpaRepository<UserCredential, Long> {
    
    // Finds the password hash record based on the associated User's ID
    Optional<UserCredential> findByUserId(Long userId);
}
