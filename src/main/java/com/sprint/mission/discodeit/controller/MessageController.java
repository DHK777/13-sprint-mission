package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Message", description = "Message API")
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

  private final MessageService messageService;

  @Operation(summary = "메시지 전송 (파일 첨부 가능)")
  @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Created")
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Message> createMessage(
      @RequestPart("messageCreateRequest") MessageCreateRequest request,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) {
    Message response = messageService.create(request, attachments);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(summary = "메시지 단건 조회")
  @GetMapping("/{messageId}")
  public ResponseEntity<Message> getMessage(@PathVariable UUID messageId) {
    return ResponseEntity.ok(messageService.read(messageId));
  }

  @Operation(summary = "채널 내 메시지 목록 조회")
  @GetMapping
  public ResponseEntity<List<Message>> getAllMessagesByChannelId(
      @RequestParam(name = "channelId") UUID channelId) {
    return ResponseEntity.ok(messageService.findAllByChannelId(channelId));
  }

  @Operation(summary = "메시지 수정")
  @PatchMapping("/{messageId}")
  public ResponseEntity<Message> updateMessage(
      @PathVariable UUID messageId,
      @RequestBody MessageUpdateRequest request) {
    Message response = messageService.update(messageId, request);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "메시지 삭제")
  @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "No Content")
  @DeleteMapping("/{messageId}")
  public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
    messageService.delete(messageId);
    return ResponseEntity.noContent().build();
  }
}