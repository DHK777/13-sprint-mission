package com.sprint.mission.discodeit.dto;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequest(
    UUID channelId,
    UUID userId,
    Instant lastReadAt
) {

}
