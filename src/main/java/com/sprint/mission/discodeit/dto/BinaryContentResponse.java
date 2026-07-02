package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.time.Instant;
import java.util.UUID;

public record BinaryContentResponse(
    UUID id,
    String fileName,
    String fileUrl,
    Long size,
    Instant createdAt
) {

  public static BinaryContentResponse from(BinaryContent entity) {
    return new BinaryContentResponse(
        entity.getId(),
        entity.getFileName(),
        entity.getFileUrl(),
        entity.getSize(),
        entity.getCreatedAt()
    );
  }
}