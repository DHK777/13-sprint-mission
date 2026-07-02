package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ChannelResponse;
import com.sprint.mission.discodeit.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Channel", description = "Channel API")
@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

  private final ChannelService channelService;

  @Operation(summary = "User가 참여 중인 Channel 목록 조회")
  @GetMapping
  public ResponseEntity<List<ChannelResponse>> getAllChannels(
      @RequestParam(name = "userId") UUID userId) {
    List<ChannelResponse> responses = channelService.findAllByUserId(userId).stream()
        .map(ChannelResponse::from).toList();
    return ResponseEntity.ok(responses);
  }

  @Operation(summary = "PUBLIC Channel 생성")
  @PostMapping("/public")
  public ResponseEntity<ChannelResponse> createPublicChannel(
      @RequestBody PublicChannelCreateRequest request) {
    Channel entity = channelService.createPublic(request.name(), request.description());
    return ResponseEntity.status(HttpStatus.CREATED).body(ChannelResponse.from(entity));
  }

  @Operation(summary = "Private Channel 생성")
  @PostMapping("/private")
  public ResponseEntity<ChannelResponse> createPrivateChannel(
      @RequestBody PrivateChannelCreateRequest request) {
    Channel entity = channelService.createPrivate(request.participantIds());
    return ResponseEntity.status(HttpStatus.CREATED).body(ChannelResponse.from(entity));
  }

  @Operation(summary = "Channel 정보 수정")
  @PatchMapping("/{channelId}")
  public ResponseEntity<ChannelResponse> updateChannel(
      @PathVariable UUID channelId, @RequestBody PublicChannelUpdateRequest request) {
    Channel entity = channelService.update(channelId, request.newName(), request.newDescription());
    return ResponseEntity.ok(ChannelResponse.from(entity));
  }

  @Operation(summary = "Channel 삭제")
  @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "No Content")
  @DeleteMapping("/{channelId}")
  public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId) {
    channelService.delete(channelId);
    return ResponseEntity.noContent().build();
  }
}