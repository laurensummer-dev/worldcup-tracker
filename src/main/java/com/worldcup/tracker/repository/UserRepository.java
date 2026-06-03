package com.worldcup.tracker.repository;

import com.worldcup.tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Method names get read and generates queries automatically
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

}
