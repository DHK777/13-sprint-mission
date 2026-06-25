package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ChannelDto;
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
  public ResponseEntity<List<ChannelDto>> getAllChannels(
      @RequestParam(name = "userId") UUID userId) {
    return ResponseEntity.ok(channelService.findAllByUserId(userId));
  }

  @Operation(summary = "Public Channel 생성")
  @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Created")
  @PostMapping("/public")
  public ResponseEntity<Channel> createPublicChannel(
      @RequestBody PublicChannelCreateRequest request) {
    Channel response = channelService.createPublicChannel(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(summary = "Private Channel 생성")
  @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Created")
  @PostMapping("/private")
  public ResponseEntity<Channel> createPrivateChannel(
      @RequestBody PrivateChannelCreateRequest request) {
    Channel response = channelService.createPrivateChannel(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(summary = "Channel 정보 수정")
  @PatchMapping("/{channelId}")
  public ResponseEntity<Channel> updateChannel(
      @PathVariable UUID channelId,
      @RequestBody PublicChannelUpdateRequest request) {
    Channel response = channelService.update(channelId, request);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Channel 삭제")
  @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "No Content")
  @DeleteMapping("/{channelId}")
  public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId) {
    channelService.delete(channelId);
    return ResponseEntity.noContent().build();
  }
}