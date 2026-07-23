package com.sprint.mission.discodeit.dto;

public record FileUploadDto(
    String fileName,
    String contentType,
    long size,
    byte[] bytes
) {

}
