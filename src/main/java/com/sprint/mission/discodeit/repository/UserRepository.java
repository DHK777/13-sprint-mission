package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);

  Optional<User> findByUsername(String username);
}