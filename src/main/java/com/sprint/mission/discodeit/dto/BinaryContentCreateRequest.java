package com.sprint.mission.discodeit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record BinaryContentCreateRequest(
    @NotBlank(message = "파일명은 필수 입력 값입니다.")
    String fileName,

    @NotBlank(message = "파일 URL은 필수 입력 값입니다.")
    String fileUrl,

    @NotNull(message = "파일 크기는 필수 입력 값입니다.")
    @PositiveOrZero(message = "파일 크기는 0 이상이어야 합니다.")
    Long size
) {

}