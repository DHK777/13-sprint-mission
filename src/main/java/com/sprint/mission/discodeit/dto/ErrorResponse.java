package com.sprint.mission.discodeit.dto;

public record ErrorResponse(
    String error,
    String details
) {

  public ErrorResponse(String error) {
    this(error, null);
  }
}