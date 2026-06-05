package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus {
    private final UUID id;
    private final UUID userId;    // 누가
    private final UUID channelId; // 어떤 채널을
    private Instant lastReadAt;   // 언제 마지막으로 읽었나
    private final Instant createdAt;
    private Instant updatedAt;

    public ReadStatus(UUID userId, UUID channelId) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = Instant.now();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    // 메시지를 읽었을 때 시간을 갱신
    public void updateLastReadAt() {
        this.lastReadAt = Instant.now();
        this.updatedAt = Instant.now();
    }
}