package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserStatusDto;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface UserStatusService {

  UserStatusDto create(UUID userId);

  UserStatusDto findById(UUID id);

  List<UserStatusDto> findAll();

  UserStatusDto update(UUID id, Instant newLastActiveAt);

  UserStatusDto updateByUserId(UUID userId, Instant newLastActiveAt);

  void delete(UUID id);
}
