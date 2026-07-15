package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "binary_contents")
@Getter
public class BinaryContent extends BaseEntity {

  @Column(name = "file_name", nullable = false)
  private String fileName;

  @Transient
  private String fileUrl;

  @Column(nullable = false)
  private Long size;

  @Column(name = "content_type", nullable = false, length = 100)
  private String contentType;

  protected BinaryContent() {
  }

  public BinaryContent(String fileName, String fileUrl, Long size) {
    this(fileName, fileUrl, size, "application/octet-stream");
  }

  public BinaryContent(String fileName, String fileUrl, Long size, String contentType) {
    this.fileName = fileName;
    this.fileUrl = fileUrl;
    this.size = size;
    this.contentType = contentType;
  }
}