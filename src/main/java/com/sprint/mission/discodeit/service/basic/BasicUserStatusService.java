package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserStatusDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.exception.UserStatusNotFoundException;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;
  private final UserStatusMapper userStatusMapper;

  @Override
  @Transactional
  public UserStatusDto create(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(Map.of("userId", userId)));

    if (userStatusRepository.findByUserId(userId).isPresent()) {
      throw new UserAlreadyExistsException(ErrorCode.DUPLICATE_USER_STATUS,
          Map.of("userId", userId));
    }

    UserStatus status = new UserStatus(user);
    userStatusRepository.save(status);

    return userStatusMapper.toDto(status);
  }

  @Override
  public UserStatusDto find(UUID id) {
    UserStatus status = userStatusRepository.findById(id)
        .orElseThrow(() -> new UserStatusNotFoundException(Map.of("statusId", id)));

    return userStatusMapper.toDto(status);
  }

  @Override
  public List<UserStatusDto> findAll() {
    return userStatusRepository.findAll().stream()
        .map(userStatusMapper::toDto)
        .toList();
  }

  @Override
  @Transactional
  public UserStatusDto update(UUID id, Instant newLastActiveAt) {
    UserStatus status = userStatusRepository.findById(id)
        .orElseThrow(() -> new UserStatusNotFoundException(Map.of("statusId", id)));

    status.updateActivity();
    return userStatusMapper.toDto(status);
  }

  @Override
  @Transactional
  public UserStatusDto updateByUserId(UUID userId, Instant newLastActiveAt) {
    UserStatus status = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new UserStatusNotFoundException(Map.of("userId", userId)));

    status.updateActivity();
    return userStatusMapper.toDto(status);
  }

  @Override
  @Transactional
  public void delete(UUID id) {
    UserStatus status = userStatusRepository.findById(id)
        .orElseThrow(() -> new UserStatusNotFoundException(Map.of("statusId", id)));

    userStatusRepository.delete(status);
  }
}