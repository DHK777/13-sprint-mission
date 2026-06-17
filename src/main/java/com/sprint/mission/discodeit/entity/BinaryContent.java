package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final String fileName; // 파일명 (예: test.txt)
    private final String fileUrl;  // 실제 저장 경로 (예: HTTP://localhost:8080/files/test.txt)
    private final Long fileSize;   // 파일 크기
    private final Instant createdAt;

    public BinaryContent(String fileName, String fileUrl, Long fileSize) {
        this.id = UUID.randomUUID();
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.fileSize = fileSize;
        this.createdAt = Instant.now();
    }
}