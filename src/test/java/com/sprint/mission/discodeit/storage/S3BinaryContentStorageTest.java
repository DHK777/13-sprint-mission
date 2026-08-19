package com.sprint.mission.discodeit.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class S3BinaryContentStorageTest {

  private static S3BinaryContentStorage storage;

  @BeforeAll
  static void setUp() throws Exception {
    Properties properties = new Properties();
    try (FileInputStream fis = new FileInputStream(".env")) {
      properties.load(fis);
    }

    String region = properties.getProperty("AWS_S3_REGION");
    String bucket = properties.getProperty("AWS_S3_BUCKET");

    storage = new S3BinaryContentStorage("", "", region, bucket, 600L);
  }

  @Test
  @DisplayName("S3 저장소 put, get, download 통합 테스트")
  void testS3Storage() throws Exception {
    UUID id = UUID.randomUUID();
    String testContent = "S3BinaryContentStorage Integration Test";
    byte[] data = testContent.getBytes(StandardCharsets.UTF_8);

    UUID savedId = storage.put(id, data);
    assertEquals(id, savedId);
    System.out.println("Put 완료: " + savedId);

    InputStream inputStream = storage.get(id);
    assertNotNull(inputStream);
    String retrievedContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    assertEquals(testContent, retrievedContent);
    System.out.println("Get 완료: " + retrievedContent);

    BinaryContentDto dto = new BinaryContentDto(id, "sample.txt", (long) data.length, "text/plain");

    ResponseEntity<?> response = storage.download(dto);
    assertEquals(HttpStatus.FOUND, response.getStatusCode());
    assertNotNull(response.getHeaders().getLocation());
    System.out.println("Download 리다이렉트 URL 발급 완료: " + response.getHeaders().getLocation());
  }
}