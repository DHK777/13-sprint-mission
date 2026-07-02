package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.UUID;

public record UserStatusResponse(
    UUID id,
    UUID userId,
    Instant lastActiveAt,
    boolean isOnline,
    Instant createdAt,
    Instant updatedAt
) {

  public static UserStatusResponse from(UserStatus entity) {
    return new UserStatusResponse(
        entity.getId(), entity.getUserId(), entity.getLastActiveAt(),
        entity.isOnline(), entity.getCreatedAt(), entity.getUpdatedAt()
    );
  }
}