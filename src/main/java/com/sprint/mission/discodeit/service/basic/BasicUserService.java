package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.FileUploadDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final UserMapper userMapper;

  @Override
  @Transactional
  public UserDto create(String email, String username, String password, FileUploadDto profile) {
    if (userRepository.existsByUsername(username)) {
      throw new IllegalArgumentException("이미 사용 중인 유저 이름입니다.");
    }
    if (userRepository.existsByEmail(email)) {
      throw new IllegalArgumentException("이미 가입된 이메일입니다.");
    }

    User user = new User(email, username, password);
    saveProfileImage(user, profile);
    userRepository.save(user);

    if (profile != null && profile.bytes() != null && profile.bytes().length > 0) {
      try {
        binaryContentStorage.put(user.getProfile().getId(), profile.bytes());
      } catch (Exception e) {
        throw new RuntimeException("프로필 이미지 저장 중 오류 발생", e);
      }
    }

    UserStatus userStatus = new UserStatus(user);
    userStatusRepository.save(userStatus);

    return userMapper.toDto(user);
  }

  @Override
  public UserDto find(UUID id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));
    return userMapper.toDto(user);
  }

  @Override
  public List<UserDto> findAll() {
    return userRepository.findAll().stream()
        .map(userMapper::toDto)
        .toList();
  }

  @Override
  public List<UserDto> findAllUsers() {
    return findAll();
  }

  @Override
  @Transactional
  public UserDto update(UUID id, String newEmail, String newUsername, String newPassword,
      String statusMessage, FileUploadDto profile) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("수정할 유저를 찾을 수 없습니다."));

    user.update(newEmail, newUsername, newPassword, statusMessage);

    if (profile != null && profile.bytes() != null && profile.bytes().length > 0) {
      saveProfileImage(user, profile);
      userRepository.saveAndFlush(user);

      try {
        binaryContentStorage.put(user.getProfile().getId(), profile.bytes());
      } catch (Exception e) {
        throw new RuntimeException("프로필 이미지 업데이트 중 오류 발생", e);
      }
    }
    return userMapper.toDto(user);
  }

  @Override
  @Transactional
  public void delete(UUID id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("삭제할 유저를 찾을 수 없습니다."));

    userStatusRepository.deleteByUserId(user.getId());
    userRepository.delete(user);
  }

  private void saveProfileImage(User user, FileUploadDto profile) {
    if (profile != null && profile.bytes() != null && profile.bytes().length > 0) {
      String fileName = profile.fileName();
      long fileSize = profile.size();
      String contentType = profile.contentType();
      String fileUrl = "/api/binaryContents/";

      BinaryContent profileImage = new BinaryContent(fileName, fileUrl, fileSize, contentType);
      user.updateProfile(profileImage);
    }
  }
}