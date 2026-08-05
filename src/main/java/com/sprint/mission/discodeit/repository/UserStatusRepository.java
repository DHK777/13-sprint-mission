package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface UserStatusRepository extends JpaRepository<UserStatus, UUID> {

  @EntityGraph(attributePaths = {"user"})
  Optional<UserStatus> findByUserId(UUID userId);

  void deleteByUserId(UUID userId);

  @NonNull
  @EntityGraph(attributePaths = {"user"})
  Optional<UserStatus> findById(@NonNull UUID id);

  @NonNull
  @EntityGraph(attributePaths = {"user"})
  List<UserStatus> findAll();
}