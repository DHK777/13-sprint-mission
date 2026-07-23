package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

  @EntityGraph(attributePaths = {"user", "channel"})
  List<ReadStatus> findByUserId(UUID userId);

  @EntityGraph(attributePaths = {"user", "channel"})
  List<ReadStatus> findByChannelId(UUID channelId);

  @EntityGraph(attributePaths = {"user", "channel"})
  Optional<ReadStatus> findByChannelIdAndUserId(UUID channelId, UUID userId);

  void deleteByChannelId(UUID channelId);
  
  @NonNull
  @EntityGraph(attributePaths = {"user", "channel"})
  Optional<ReadStatus> findById(@NonNull UUID id);

  @NonNull
  @EntityGraph(attributePaths = {"user", "channel"})
  List<ReadStatus> findAll();
}