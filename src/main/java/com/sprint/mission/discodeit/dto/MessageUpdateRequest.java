package com.sprint.mission.discodeit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MessageUpdateRequest(
    @NotBlank(message = "수정할 메시지 내용은 필수 입력 값입니다.")
    @Size(max = 2000, message = "메시지는 2000자를 초과할 수 없습니다.")
    String newContent
) {

}