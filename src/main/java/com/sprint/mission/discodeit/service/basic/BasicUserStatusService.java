package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserStatusDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.List;
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
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

    if (userStatusRepository.findByUserId(userId).isPresent()) {
      throw new IllegalArgumentException("해당 유저의 상태 정보가 이미 존재합니다.");
    }

    UserStatus status = new UserStatus(user);
    userStatusRepository.save(status);

    return userStatusMapper.toDto(status);
  }

  @Override
  public UserStatusDto find(UUID id) {
    UserStatus status = userStatusRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("상태 정보를 찾을 수 없습니다."));

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
        .orElseThrow(() -> new IllegalArgumentException("상태 정보를 찾을 수 없습니다."));

    status.updateActivity();
    return userStatusMapper.toDto(status);
  }

  @Override
  @Transactional
  public UserStatusDto updateByUserId(UUID userId, Instant newLastActiveAt) {
    UserStatus status = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new IllegalArgumentException("해당 유저의 상태 정보를 찾을 수 없습니다."));

    status.updateActivity();
    return userStatusMapper.toDto(status);
  }

  @Override
  @Transactional
  public void delete(UUID id) {
    UserStatus status = userStatusRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("상태 정보를 찾을 수 없습니다."));

    userStatusRepository.delete(status);
  }
}