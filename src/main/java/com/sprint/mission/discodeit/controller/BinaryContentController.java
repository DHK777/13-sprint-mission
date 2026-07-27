package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "BinaryContent", description = "첨부파일 메타데이터 API")
@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController {

  private final BinaryContentService binaryContentService;
  private final BinaryContentStorage binaryContentStorage;

  @Operation(summary = "첨부파일 메타데이터 생성")
  @PostMapping
  public ResponseEntity<BinaryContentDto> createBinaryContent(
      @RequestBody BinaryContentCreateRequest request) {
    BinaryContentDto response = binaryContentService.create(
        request.fileName(),
        request.fileUrl(),
        request.size()
    );
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(summary = "첨부파일 메타데이터 단건 조회")
  @GetMapping("/{binaryContentId}")
  public ResponseEntity<BinaryContentDto> getBinaryContent(
      @PathVariable UUID binaryContentId) {
    BinaryContentDto response = binaryContentService.find(binaryContentId);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "첨부파일 메타데이터 다건(목록) 조회")
  @GetMapping
  public ResponseEntity<List<BinaryContentDto>> getBinaryContents(
      @RequestParam(name = "binaryContentIds", required = false) List<UUID> binaryContentIds) {

    if (binaryContentIds == null || binaryContentIds.isEmpty()) {
      return ResponseEntity.ok(java.util.Collections.emptyList());
    }

    List<BinaryContentDto> responses = binaryContentService.findAllByIdIn(binaryContentIds);
    return ResponseEntity.ok(responses);
  }

  @Operation(summary = "파일 다운로드")
  @GetMapping("/{binaryContentId}/download")
  public ResponseEntity<Resource> download(@PathVariable UUID binaryContentId) {
    log.debug("파일 다운로드 요청 - binaryContentId: {}", binaryContentId);

    try {
      BinaryContentDto dto = binaryContentService.find(binaryContentId);
      Resource resource = binaryContentStorage.download(binaryContentId);

      String encodedFileName = URLEncoder.encode(dto.fileName(), StandardCharsets.UTF_8)
          .replace("+", "%20");

      log.info("파일 다운로드 성공 - fileName: {}, size: {}", dto.fileName(), dto.size());

      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION,
              "attachment; filename*=UTF-8''" + encodedFileName)
          .contentType(MediaType.parseMediaType(dto.contentType()))
          .contentLength(dto.size())
          .body(resource);

    } catch (Exception e) {
      log.error("파일 다운로드 중 서버 오류 발생 - binaryContentId: {}", binaryContentId, e);
      throw new RuntimeException("파일 다운로드 중 오류가 발생했습니다.", e);
    }
  }
}