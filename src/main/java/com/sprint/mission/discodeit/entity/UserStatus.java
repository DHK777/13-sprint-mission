package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID id; // UserStatus 고유 식별자, 이 기록 자체의 고유 번호
    private final UUID userId; // 추적하는 대상의 userid
    private Instant lastActiveAt; // 유저가 활동한 마지막 시간
    private final Instant createdAt;
    private Instant updatedAt; // 시스템이 데이터를 수정한 시간

    public UserStatus(UUID userId) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.lastActiveAt = Instant.now();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    // 활동 시간을 현재 시간으로 갱신
    public void updateActivity() {
        this.lastActiveAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    // 마지막 접속 시간이 현재 시간으로부터 5분 이내이면 true 반환
    public boolean isOnline() {
        // 현재 시간에서 5분을 뺀 기준 시간 설정
        Instant fiveMinutesAgo = Instant.now().minus(Duration.ofMinutes(5));
        // 마지막 활동 시간이 5분 전(기준 시간) 이후면 접속 중인 것으로 판단
        return this.lastActiveAt.isAfter(fiveMinutesAgo);
    }
}
