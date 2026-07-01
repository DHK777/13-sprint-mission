package com.sprint.mission.discodeit.dto;

public record BinaryContentCreateRequest(
    String fileName,
    String fileUrl,
    Long size
) {

}
