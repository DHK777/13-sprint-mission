package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserResponse;
import com.sprint.mission.discodeit.dto.UserStatusResponse;
import com.sprint.mission.discodeit.dto.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Tag(name = "User", description = "User API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final UserStatusService userStatusService;

  @Operation(summary = "전체 User 목록 조회")
  @GetMapping
  public ResponseEntity<List<UserResponse>> getAllUsers() {

    List<UserResponse> responses = userService.findAllUsers().stream()
        .map(UserResponse::from)
        .toList();

    return ResponseEntity.ok(responses);
  }

  @Operation(summary = "User 등록")
  @io.swagger.v3.oas.annotations.parameters.RequestBody(
      content = @Content(
          mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
          encoding = @Encoding(
              name = "userCreateRequest",
              contentType = MediaType.APPLICATION_JSON_VALUE
          )
      )
  )
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserResponse> createUser(
      @RequestPart("userCreateRequest") UserCreateRequest request,
      @RequestPart(value = "profile", required = false) MultipartFile profile) {

    User entity = userService.create(
        request.email(),
        request.username(),
        request.password(),
        profile
    );
    return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(entity));
  }

  @Operation(summary = "User 정보 수정")
  @io.swagger.v3.oas.annotations.parameters.RequestBody(
      content = @Content(
          mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
          encoding = @Encoding(
              name = "userUpdateRequest",
              contentType = MediaType.APPLICATION_JSON_VALUE
          )
      )
  )
  @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserResponse> updateUser(
      @PathVariable UUID userId,
      @RequestPart("userUpdateRequest") UserUpdateRequest request,
      @RequestPart(value = "profile", required = false) MultipartFile profile) {

    User entity = userService.update(
        userId,
        request.newEmail(),
        request.newUsername(),
        request.newPassword(),
        request.statusMessage(),
        profile
    );
    return ResponseEntity.ok(UserResponse.from(entity));
  }

  @Operation(summary = "User 삭제")
  @ApiResponse(responseCode = "204", description = "No Content")
  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
    userService.delete(userId);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "User 온라인 상태 업데이트")
  @PatchMapping("/{userId}/userStatus")
  public ResponseEntity<UserStatusResponse> updateUserStatus(
      @PathVariable UUID userId, @RequestBody UserStatusUpdateRequest request) {
    UserStatus entity = userStatusService.updateByUserId(userId, request.newLastActiveAt());
    return ResponseEntity.ok(UserStatusResponse.from(entity));
  }
}
