package com.sprint.mission.discodeit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PublicChannelCreateRequest(
    @NotBlank(message = "채널 이름은 필수 입력 값입니다.")
    @Size(min = 2, max = 50, message = "채널 이름은 2자 이상 50자 이하로 입력해주세요.")
    String name,

    @Size(max = 1000, message = "채널 설명은 1000자를 초과할 수 없습니다.")
    String description
) {

}