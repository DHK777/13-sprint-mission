package com.sprint.mission.discodeit.dto;

public record AttachmentRequest(
        String fileName,
        String fileUrl,
        Long fileSize
) {}