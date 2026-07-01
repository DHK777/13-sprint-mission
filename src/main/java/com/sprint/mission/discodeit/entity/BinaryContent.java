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
  private final String fileUrl; // 실제 저장 경로 (예: HTTP://localhost:8080/files/test.txt)

  private final Long size; // 파일 크기
  private final byte[] bytes;
  private final String contentType;

  private final Instant createdAt;

  public BinaryContent(String fileName, String fileUrl, Long size) {
    this(fileName, fileUrl, size, null, "application/octet-stream");
  }

  public BinaryContent(String fileName, String fileUrl, Long size, byte[] bytes,
      String contentType) {
    this.id = UUID.randomUUID();
    this.fileName = fileName;
    this.fileUrl = fileUrl;
    this.size = size;
    this.bytes = bytes;
    this.contentType = contentType;
    this.createdAt = Instant.now();
  }
}