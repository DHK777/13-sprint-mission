package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ErrorResponse;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException e) {
    log.warn("Business Exception: {} - message: {}, details: {}",
        e.getClass().getSimpleName(), e.getMessage(), e.getDetails());

    ErrorResponse response = ErrorResponse.from(e);

    return ResponseEntity
        .status(e.getErrorCode().getStatus())
        .body(response);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
    log.warn("Illegal Argument: {}", e.getMessage());

    ErrorResponse response = ErrorResponse.of(
        "BAD_REQUEST",
        e.getMessage() != null ? e.getMessage() : "잘못된 요청 파라미터입니다.",
        e.getClass().getSimpleName(),
        HttpStatus.BAD_REQUEST.value()
    );

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(response);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneralException(Exception e) {
    log.error("Internal Server Error: {}", e.getMessage(), e);

    ErrorResponse response = ErrorResponse.of(
        "INTERNAL_SERVER_ERROR",
        "서버 내부 오류가 발생했습니다.",
        e.getClass().getSimpleName(),
        HttpStatus.INTERNAL_SERVER_ERROR.value()
    );

    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(response);
  }
}
