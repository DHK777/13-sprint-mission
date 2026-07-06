package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final BinaryContentStorage binaryContentStorage;

  @Override
  public User create(String email, String username, String password, MultipartFile profile) {
    if (userRepository.existsByUsername(username)) {
      throw new IllegalArgumentException("이미 사용 중인 유저 이름입니다.");
    }
    if (userRepository.existsByEmail(email)) {
      throw new IllegalArgumentException("이미 가입된 이메일입니다.");
    }

    User user = new User(email, username, password);
    saveProfileImage(user, profile);
    userRepository.save(user);

    if (profile != null && !profile.isEmpty()) {
      try {
        binaryContentStorage.put(user.getProfile().getId(), profile.getBytes());
      } catch (Exception e) {
        throw new RuntimeException("프로필 이미지 저장 중 오류 발생", e);
      }
    }

    UserStatus userStatus = new UserStatus(user);
    userStatusRepository.save(userStatus);
    return user;
  }

  @Override
  @Transactional(readOnly = true)
  public User find(UUID id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));
  }

  @Override
  @Transactional(readOnly = true)
  public List<User> findAll() {
    return userRepository.findAll();
  }

  @Override
  public User update(UUID id, String newEmail, String newUsername, String newPassword,
      String statusMessage, MultipartFile profile) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("수정할 유저를 찾을 수 없습니다."));

    user.update(newEmail, newUsername, newPassword, statusMessage);

    if (profile != null && !profile.isEmpty()) {
      saveProfileImage(user, profile);
      userRepository.saveAndFlush(user);

      try {
        binaryContentStorage.put(user.getProfile().getId(), profile.getBytes());
      } catch (Exception e) {
        throw new RuntimeException("프로필 이미지 업데이트 중 오류 발생", e);
      }
    }
    return user;
  }

  @Override
  public void delete(UUID id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("삭제할 유저를 찾을 수 없습니다."));

    userStatusRepository.deleteByUserId(user.getId());
    userRepository.delete(user);
  }

  @Override
  @Transactional(readOnly = true)
  public List<User> findAllUsers() {
    return userRepository.findAll();
  }

  private void saveProfileImage(User user, MultipartFile profile) {
    if (profile != null && !profile.isEmpty()) {
      String fileName = profile.getOriginalFilename();
      long fileSize = profile.getSize();
      String contentType = profile.getContentType();
      String fileUrl = "/api/binaryContents/";

      BinaryContent profileImage = new BinaryContent(fileName, fileUrl, fileSize, contentType);
      user.updateProfile(profileImage);
    }
  }
}