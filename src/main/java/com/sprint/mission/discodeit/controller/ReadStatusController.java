package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "ReadStatus", description = "읽음 상태 API")
@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor

public class ReadStatusController {

  private final ReadStatusService readStatusService;

  @Operation(summary = "읽음 상태 생성")
  @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Created")
  @PostMapping
  public ResponseEntity<ReadStatus> createReadStatus(@RequestBody ReadStatusCreateRequest request) {
    ReadStatus response = readStatusService.create(request.channelId(), request.userId(),
        request.lastReadAt());
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(summary = "특정 유저의 읽음 상태 목록 조회")
  @GetMapping
  public ResponseEntity<List<ReadStatus>> getReadStatuses(
      @RequestParam(name = "userId") UUID userId) {
    return ResponseEntity.ok(readStatusService.findAllByUserId(userId));
  }

  @Operation(summary = "읽음 상태 업데이트 (마지막 읽은 시간 갱신)")
  @PatchMapping("/{statusId}")
  public ResponseEntity<ReadStatus> updateReadStatus(
      @PathVariable UUID statusId,
      @RequestBody ReadStatusUpdateRequest request) {
    ReadStatus response = readStatusService.update(statusId, request.newLastReadAt());
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "읽음 상태 삭제")
  @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "No Content")
  @DeleteMapping("/{statusId}")
  public ResponseEntity<Void> deleteReadStatus(@PathVariable UUID statusId) {
    readStatusService.delete(statusId);
    return ResponseEntity.noContent().build();
  }
}