package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record ReadStatusCreateRequest(
        UUID channelId,
        UUID userId
) {}
