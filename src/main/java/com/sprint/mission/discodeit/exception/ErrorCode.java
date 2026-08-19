package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U-001", "해당 유저를 찾을 수 없습니다."),
  DUPLICATE_USER(HttpStatus.CONFLICT, "U-002", "이미 존재하는 유저입니다."),
  DUPLICATE_USERNAME(HttpStatus.CONFLICT, "U-003", "이미 사용 중인 유저 이름입니다."),
  DUPLICATE_EMAIL(HttpStatus.CONFLICT, "U-004", "이미 가입된 이메일입니다."),
  INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "U-005", "비밀번호가 일치하지 않습니다."),

  CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "C-001", "채널을 찾을 수 없습니다."),
  PRIVATE_CHANNEL_UPDATE(HttpStatus.BAD_REQUEST, "C-002", "Private 채널은 수정할 수 없습니다."),

  MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "M-001", "메시지를 찾을 수 없습니다."),

  USER_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "S-001", "상태 정보를 찾을 수 없습니다."),
  DUPLICATE_USER_STATUS(HttpStatus.CONFLICT, "S-002", "해당 유저의 상태 정보가 이미 존재합니다."),
  READ_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "S-003", "상태창을 찾을 수 없습니다."),
  DUPLICATE_READ_STATUS(HttpStatus.CONFLICT, "S-004", "해당 유저는 이미 이 채널의 읽음 상태를 가지고 있습니다."),

  BINARY_CONTENT_NOT_FOUND(HttpStatus.NOT_FOUND, "B-001", "해당 첨부파일을 찾을 수 없습니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;
}