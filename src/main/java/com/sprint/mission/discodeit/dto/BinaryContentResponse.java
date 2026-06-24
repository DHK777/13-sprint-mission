package com.sprint.mission.discodeit.dto;

import java.time.Instant;
import java.util.UUID;

public record BinaryContentResponse(
        UUID id,
        String fileName,
        String fileUrl,
        Long fileSize,
        Instant createdAt
) {}
