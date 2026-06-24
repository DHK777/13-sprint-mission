package com.sprint.mission.discodeit.dto;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusResponse(
        UUID id,
        UUID channelId,
        UUID userId,
        Instant lastReadAt
) {}
