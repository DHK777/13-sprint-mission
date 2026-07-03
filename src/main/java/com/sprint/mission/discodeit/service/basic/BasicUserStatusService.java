package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;

  @Override
  public UserStatus create(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

    if (userStatusRepository.findByUserId(userId).isPresent()) {
      throw new IllegalArgumentException("해당 유저의 상태 정보가 이미 존재합니다.");
    }

    UserStatus status = new UserStatus(user);
    userStatusRepository.save(status);
    return status;
  }

  @Override
  @Transactional(readOnly = true)
  public UserStatus find(UUID id) {
    return userStatusRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("상태 정보를 찾을 수 없습니다."));
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserStatus> findAll() {
    return userStatusRepository.findAll();
  }

  @Override
  public UserStatus update(UUID id, Instant newLastActiveAt) {
    UserStatus status = userStatusRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("상태 정보를 찾을 수 없습니다."));
    status.updateActivity();
    return status;
  }

  @Override
  public UserStatus updateByUserId(UUID userId, Instant newLastActiveAt) {
    UserStatus status = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new IllegalArgumentException("해당 유저의 상태 정보를 찾을 수 없습니다."));
    status.updateActivity();
    return status;
  }

  @Override
  public void delete(UUID id) {
    UserStatus status = userStatusRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("상태 정보를 찾을 수 없습니다."));
    userStatusRepository.delete(status);
  }
}