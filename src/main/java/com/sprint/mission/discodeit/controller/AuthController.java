package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.LoginRequest;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "인증 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @Operation(summary = "로그인")
  @PostMapping("/login")
  public ResponseEntity<UserDto> login(@RequestBody LoginRequest request) {

    User loginUser = authService.login(request.username(), request.password());

    UserDto response = new UserDto(
        loginUser.getId(),
        loginUser.getCreatedAt(),
        loginUser.getUpdatedAt(),
        loginUser.getUsername(),
        loginUser.getEmail(),
        loginUser.getProfileId(),
        null
    );

    return ResponseEntity.ok(response);
  }
}