package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Message;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageResponse(
    UUID id,
    UUID channelId,
    UUID authorId,
    String content,
    List<UUID> attachmentIds,
    Instant createdAt,
    Instant updatedAt
) {

  public static MessageResponse from(Message entity) {
    return new MessageResponse(
        entity.getId(), entity.getChannelId(), entity.getAuthorId(),
        entity.getContent(), entity.getAttachmentIds(),
        entity.getCreatedAt(), entity.getUpdatedAt()
    );
  }
}