package com.sprint.mission.discodeit.storage.s3;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Properties;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

public class AWSS3Test {

  private static S3Client s3Client;
  private static S3Presigner s3Presigner;
  private static String bucketName;

  @BeforeAll
  static void setUp() throws Exception {
    Properties properties = new Properties();
    try (FileInputStream fis = new FileInputStream(".env")) {
      properties.load(fis);
    }

    String regionString = properties.getProperty("AWS_S3_REGION");
    bucketName = properties.getProperty("AWS_S3_BUCKET");
    Region region = Region.of(regionString);

    s3Client = S3Client.builder()
        .region(region)
        .credentialsProvider(DefaultCredentialsProvider.create())
        .build();

    s3Presigner = S3Presigner.builder()
        .region(region)
        .credentialsProvider(DefaultCredentialsProvider.create())
        .build();
  }

  @Test
  @DisplayName("S3 파일 업로드 테스트")
  void testUpload() {
    String key = "test-folder/sample.txt";
    Path filePath = Paths.get("settings.gradle");

    PutObjectRequest putOb = PutObjectRequest.builder()
        .bucket(bucketName)
        .key(key)
        .build();

    s3Client.putObject(putOb, filePath);
    System.out.println("업로드 성공. S3 경로: " + key);
  }

  @Test
  @DisplayName("S3 파일 다운로드 테스트")
  void testDownload() throws Exception {
    String key = "test-folder/sample.txt";
    Path downloadPath = Paths.get("downloaded-sample.txt");

    java.nio.file.Files.deleteIfExists(downloadPath);

    GetObjectRequest getOb = GetObjectRequest.builder()
        .bucket(bucketName)
        .key(key)
        .build();

    s3Client.getObject(getOb, downloadPath);
    System.out.println("다운로드 성공. 로컬 경로: " + downloadPath.toAbsolutePath());

    assertTrue(downloadPath.toFile().exists());
  }

  @Test
  @DisplayName("S3 Presigned URL 생성 테스트")
  void testPresignedUrl() {
    String key = "test-folder/sample.txt";

    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucketName)
        .key(key)
        .build();

    GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
        .signatureDuration(Duration.ofMinutes(10))
        .getObjectRequest(getObjectRequest)
        .build();

    PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(
        getObjectPresignRequest);
    String url = presignedRequest.url().toString();

    System.out.println("Presigned URL 발급 성공: " + url);
    assertNotNull(url);
  }
}