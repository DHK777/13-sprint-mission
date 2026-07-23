package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.FileUploadDto;
import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Instant;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
  @io.swagger.v3.oas.annotations.parameters.RequestBody(
      content = @Content(
          mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
          encoding = @Encoding(
              name = "messageCreateRequest",
              contentType = MediaType.APPLICATION_JSON_VALUE
          )
      )
  )
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<MessageDto> createMessage(
      @RequestPart("messageCreateRequest") MessageCreateRequest request,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) {

    List<FileUploadDto> fileDtos = null;
    if (attachments != null) {
      fileDtos = new ArrayList<>();
      for (MultipartFile file : attachments) {
        if (!file.isEmpty()) {
          try {
            fileDtos.add(new FileUploadDto(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getSize(),
                file.getBytes()
            ));
          } catch (Exception e) {
            throw new RuntimeException("파일 읽기 실패", e);
          }
        }
      }
    }

    MessageDto response = messageService.create(
        request.channelId(),
        request.authorId(),
        request.content(),
        fileDtos
    );
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(summary = "메시지 단건 조회")
  @GetMapping("/{messageId}")
  public ResponseEntity<MessageDto> getMessage(@PathVariable UUID messageId) {
    return ResponseEntity.ok(messageService.read(messageId));
  }

  @Operation(summary = "채널 내 메시지 목록 조회")
  @GetMapping
  public ResponseEntity<PageResponse<MessageDto>> getAllMessagesByChannelId(
      @RequestParam(name = "channelId") UUID channelId,
      @RequestParam(name = "cursor", required = false) Instant cursor,
      @PageableDefault(size = 50) Pageable pageable
  ) {
    PageResponse<MessageDto> response = messageService.findAllByChannelId(channelId, cursor,
        pageable);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "메시지 수정")
  @PatchMapping("/{messageId}")
  public ResponseEntity<MessageDto> updateMessage(
      @PathVariable UUID messageId, @RequestBody MessageUpdateRequest request) {
    MessageDto response = messageService.update(messageId, request.newContent());
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "메시지 삭제")
  @ApiResponse(responseCode = "204", description = "No Content")
  @DeleteMapping("/{messageId}")
  public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
    messageService.delete(messageId);
    return ResponseEntity.noContent().build();
  }
}