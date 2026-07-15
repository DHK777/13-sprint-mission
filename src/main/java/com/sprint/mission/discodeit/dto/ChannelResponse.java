package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.time.Instant;
import java.util.UUID;

public record ChannelResponse(
    UUID id,
    ChannelType type,
    String name,
    String description,
    Instant createdAt,
    Instant updatedAt
) {

  public static ChannelResponse from(Channel entity) {
    return new ChannelResponse(
        entity.getId(), entity.getType(), entity.getName(),
        entity.getDescription(), entity.getCreatedAt(), entity.getUpdatedAt()
    );
  }
}
