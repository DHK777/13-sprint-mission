package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "BinaryContent", description = "첨부파일 메타데이터 API")
@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController {

  private final BinaryContentService binaryContentService;

  @Operation(summary = "첨부파일 메타데이터 생성")
  @PostMapping
  public ResponseEntity<BinaryContent> createBinaryContent(
      @RequestBody BinaryContentCreateRequest request) {
    BinaryContent response = binaryContentService.create(
        request.fileName(),
        request.fileUrl(),
        request.size()
    );
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(summary = "첨부파일 메타데이터 단건 조회")
  @GetMapping("/{binaryContentId}")
  public ResponseEntity<BinaryContent> getBinaryContent(@PathVariable UUID binaryContentId) {
    return ResponseEntity.ok(binaryContentService.find(binaryContentId));
  }

  @Operation(summary = "첨부파일 메타데이터 다건(목록) 조회")
  @GetMapping
  public ResponseEntity<List<BinaryContent>> getBinaryContents(
      @RequestParam(name = "binaryContentIds", required = false) List<UUID> binaryContentIds) {
    if (binaryContentIds == null || binaryContentIds.isEmpty()) {
      return ResponseEntity.ok(java.util.Collections.emptyList());
    }
    return ResponseEntity.ok(binaryContentService.findAllByIdIn(binaryContentIds));
  }
}