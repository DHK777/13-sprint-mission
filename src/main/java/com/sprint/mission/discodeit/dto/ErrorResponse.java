package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import java.time.Instant;
import java.util.Map;

public record ErrorResponse(
    Instant timestamp,
    String code,
    String message,
    Map<String, Object> details,
    String exceptionType,
    int status
) {

  public static ErrorResponse from(DiscodeitException e) {
    return new ErrorResponse(
        e.getTimestamp(),
        e.getErrorCode().name(),
        e.getErrorCode().getMessage(),
        e.getDetails(),
        e.getClass().getSimpleName(),
        e.getErrorCode().getStatus().value()
    );
  }

  public static ErrorResponse of(String code, String message, String exceptionType, int status) {
    return new ErrorResponse(
        Instant.now(),
        code,
        message,
        null,
        exceptionType,
        status
    );
  }
}