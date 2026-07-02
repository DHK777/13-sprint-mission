package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.time.Instant;
import java.util.UUID;

public record ReadStatusResponse(
    UUID id,
    UUID userId,
    UUID channelId,
    Instant lastReadAt,
    Instant createdAt,
    Instant updatedAt
) {

  public static ReadStatusResponse from(ReadStatus entity) {
    return new ReadStatusResponse(
        entity.getId(), entity.getUserId(), entity.getChannelId(),
        entity.getLastReadAt(), entity.getCreatedAt(), entity.getUpdatedAt()
    );
  }
}