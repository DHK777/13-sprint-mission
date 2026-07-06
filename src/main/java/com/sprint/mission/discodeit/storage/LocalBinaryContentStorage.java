package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private final Path root;

  public LocalBinaryContentStorage(@Value("${discodeit.storage.local.root-path}") String rootPath) {
    this.root = Paths.get(rootPath);
  }

  @PostConstruct
  public void init() {
    try {
      if (!Files.exists(root)) {
        Files.createDirectories(root);
      }
    } catch (IOException e) {
      throw new RuntimeException("로컬 저장소 디렉토리를 생성할 수 없습니다.", e);
    }
  }

  private Path resolvePath(UUID id) {
    return root.resolve(id.toString());
  }

  @Override
  public UUID put(UUID id, byte[] data) {
    try {
      Path filePath = resolvePath(id);
      Files.write(filePath, data);
      return id;
    } catch (IOException e) {
      throw new RuntimeException("파일 저장 실패: " + id, e);
    }
  }

  @Override
  public InputStream get(UUID id) {
    try {
      Path filePath = resolvePath(id);
      return Files.newInputStream(filePath);
    } catch (IOException e) {
      throw new RuntimeException("파일 읽기 실패: " + id, e);
    }
  }

  @Override
  public ResponseEntity<Resource> download(BinaryContentDto dto) {
    try {
      InputStream inputStream = get(dto.id());
      Resource resource = new InputStreamResource(inputStream);

      String encodedFileName = URLEncoder.encode(dto.fileName(), StandardCharsets.UTF_8)
          .replace("+", "%20");

      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION,
              "attachment; filename*=UTF-8''" + encodedFileName)
          .contentType(MediaType.parseMediaType(dto.contentType()))
          .contentLength(dto.size())
          .body(resource);

    } catch (Exception e) {
      return ResponseEntity.internalServerError().build();
    }
  }
}