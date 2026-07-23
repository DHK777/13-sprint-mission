package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ReadStatusDto;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

  ReadStatusDto create(UUID channelId, UUID userId, Instant lastReadAt);

  ReadStatusDto find(UUID id);

  List<ReadStatusDto> findAllByUserId(UUID userId);

  ReadStatusDto update(UUID id, Instant newLastReadAt);

  void delete(UUID id);
}
