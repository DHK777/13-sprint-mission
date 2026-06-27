package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

  ReadStatus create(UUID channelId, UUID userId, Instant lastReadAt);

  ReadStatus find(UUID id);

  List<ReadStatus> findAllByUserId(UUID userId);

  ReadStatus update(UUID id, Instant newLastReadAt);

  void delete(UUID id);
}
