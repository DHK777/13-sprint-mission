package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface BinaryContentRepository extends JpaRepository<BinaryContent, UUID> {

  @NonNull
  Optional<BinaryContent> findById(@NonNull UUID id);

  @NonNull
  List<BinaryContent> findAll();
}