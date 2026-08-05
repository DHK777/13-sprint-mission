package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

  @Operation(summary = "PUBLIC Channel 생성")
  @PostMapping("/public")
  public ResponseEntity<ChannelDto> createPublicChannel(
      @Valid @RequestBody PublicChannelCreateRequest request) {
    ChannelDto response = channelService.createPublic(request.name(), request.description());
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(summary = "Private Channel 생성")
  @PostMapping("/private")
  public ResponseEntity<ChannelDto> createPrivateChannel(
      @Valid @RequestBody PrivateChannelCreateRequest request) {
    ChannelDto response = channelService.createPrivate(request.participantIds());
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(summary = "Channel 정보 수정")
  @PatchMapping("/{channelId}")
  public ResponseEntity<ChannelDto> updateChannel(
      @PathVariable UUID channelId, @Valid @RequestBody PublicChannelUpdateRequest request) {
    ChannelDto response = channelService.update(channelId, request.newName(),
        request.newDescription());
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Channel 삭제")
  @ApiResponse(responseCode = "204", description = "No Content")
  @DeleteMapping("/{channelId}")
  public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId) {
    channelService.delete(channelId);
    return ResponseEntity.noContent().build();
  }
}