package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final UserStatusRepository userStatusRepository;

  @Override
  public User create(UserCreateRequest request, MultipartFile profile) {
    if (userRepository.existsByUsername(request.username())) {
      throw new IllegalArgumentException("이미 사용 중인 유저 이름입니다.");
    }
    if (userRepository.existsByEmail(request.email())) {
      throw new IllegalArgumentException("이미 가입된 이메일입니다.");
    }

    User user = new User(request.email(), request.username(), request.password());

    saveProfileImage(user, profile);
    userRepository.save(user);

    UserStatus userStatus = new UserStatus(user.getId());
    userStatusRepository.save(userStatus);
    return user;
  }

  @Override
  public User find(UUID id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));
  }

  @Override
  public List<User> findAll() {
    return userRepository.findAll();
  }

  @Override
  public User update(UUID id, UserUpdateRequest request, MultipartFile profile) {

    User user = userRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("수정할 유저를 찾을 수 없습니다."));

    user.update(
        request.newEmail(),
        request.newUsername(),
        request.newPassword(),
        request.statusMessage()
    );

    saveProfileImage(user, profile);

    userRepository.save(user);
    return user;
  }

  @Override
  public void delete(UUID id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("삭제할 유저를 찾을 수 없습니다."));

    if (user.getProfileId() != null) {
      binaryContentRepository.deleteById(user.getProfileId());
    }

    userStatusRepository.deleteByUserId(user.getId());
    userRepository.delete(user.getId());
  }

  @Override
  public List<UserDto> findAllUsers() {
    return userRepository.findAll().stream()
        .map(user -> {
          boolean isOnline = userStatusRepository.findByUserId(user.getId())
              .map(UserStatus::isOnline)
              .orElse(false);

          return new UserDto(
              user.getId(),
              user.getCreatedAt(),
              user.getUpdatedAt(),
              user.getUsername(),
              user.getEmail(),
              user.getProfileId(),
              isOnline
          );
        })
        .toList();
  }

  private void saveProfileImage(User user, MultipartFile profile) {
    if (profile != null && !profile.isEmpty()) {
      String fileName = profile.getOriginalFilename();
      long fileSize = profile.getSize();
      String fileUrl = "/files/" + fileName;

      BinaryContent profileImage = new BinaryContent(fileName, fileUrl, fileSize);
      binaryContentRepository.save(profileImage);
      user.updateProfile(profileImage.getId());
    }
  }
}