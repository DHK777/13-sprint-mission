package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelResponse(
        UUID id,
        String name,
        ChannelType type,
        String description,
        Instant lastMessageTime, // 최근 메시지 시간
        List<UUID> userIds // PRIVATE 채널일 경우 참여한 유저 ID 목록 포함 (퍼블릭이면 null)
) {}
