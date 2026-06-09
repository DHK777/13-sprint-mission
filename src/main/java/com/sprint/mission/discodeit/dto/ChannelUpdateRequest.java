package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.ChannelType;

public record ChannelUpdateRequest(
        String name,
        ChannelType type,
        String description
) {}
