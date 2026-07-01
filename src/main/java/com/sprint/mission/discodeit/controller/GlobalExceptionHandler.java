package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({NoSuchElementException.class, RuntimeException.class})
  public ResponseEntity<ErrorResponse> handleNotFoundException(Exception e) {
    String message = e.getMessage() != null ? e.getMessage() : "데이터를 찾을 수 없습니다.";
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ErrorResponse(message));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse(e.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneralException(Exception e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse("서버 내부 오류가 발생했습니다.", e.getMessage()));
  }
}
