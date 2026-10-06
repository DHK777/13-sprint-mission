package com.sprint.mission.discodeit.dto;

import jakarta.validation.constraints.PastOrPresent;
import java.time.Instant;

public record ReadStatusUpdateRequest(
    @PastOrPresent(message = "마지막 읽은 시간은 과거 또는 현재 시간이어야 합니다.")
    Instant newLastReadAt,
    Boolean newNotificationEnabled
) {

}