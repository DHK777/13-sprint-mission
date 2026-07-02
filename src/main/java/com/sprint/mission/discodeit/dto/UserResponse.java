package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.UUID;

public record UserResponse(
    UUID id,
    String email,
    String username,
    String statusMessage,
    UUID profileId,
    Instant createdAt,
    Instant updatedAt
) {

  public static UserResponse from(User entity) {
    return new UserResponse(
        entity.getId(), entity.getEmail(), entity.getUsername(),
        entity.getStatusMessage(), entity.getProfileId(),
        entity.getCreatedAt(), entity.getUpdatedAt()
    );
  }
}