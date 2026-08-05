package com.sprint.mission.discodeit.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.Instant;

public record ReadStatusUpdateRequest(
    @NotNull(message = "마지막 읽은 시간은 필수 입력 값입니다.")
    @PastOrPresent(message = "마지막 읽은 시간은 과거 또는 현재 시간이어야 합니다.")
    Instant newLastReadAt
) {

}