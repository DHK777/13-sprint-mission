package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.service.ReadStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ReadStatus", description = "읽음 상태 API")
@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  @Operation(summary = "읽음 상태 생성")
  @PostMapping
  public ResponseEntity<ReadStatusDto> createReadStatus(
      @RequestBody ReadStatusCreateRequest request) {
    ReadStatusDto response = readStatusService.create(
        request.channelId(), request.userId(), request.lastReadAt()
    );
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(summary = "특정 유저의 읽음 상태 목록 조회")
  @GetMapping
  public ResponseEntity<List<ReadStatusDto>> getReadStatuses(
      @RequestParam(name = "userId") UUID userId) {
    return ResponseEntity.ok(readStatusService.findAllByUserId(userId));
  }

  @Operation(summary = "읽음 상태 업데이트 (마지막 읽은 시간 갱신)")
  @PatchMapping("/{statusId}")
  public ResponseEntity<ReadStatusDto> updateReadStatus(
      @PathVariable UUID statusId, @RequestBody ReadStatusUpdateRequest request) {
    ReadStatusDto response = readStatusService.update(statusId, request.newLastReadAt());
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "읽음 상태 삭제")
  @ApiResponse(responseCode = "204", description = "No Content")
  @DeleteMapping("/{statusId}")
  public ResponseEntity<Void> deleteReadStatus(@PathVariable UUID statusId) {
    readStatusService.delete(statusId);
    return ResponseEntity.noContent().build();
  }
}