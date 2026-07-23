package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface ChannelRepository extends JpaRepository<Channel, UUID> {

  @NonNull
  Optional<Channel> findById(@NonNull UUID id);

  @NonNull
  List<Channel> findAll();
}