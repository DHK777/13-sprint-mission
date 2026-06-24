package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UserResponse;
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

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public UserResponse create(UserCreateRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("이미 사용 중인 유저 이름입니다.");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        User user = new User(request.email(), request.username(), request.password());

        if (request.profileFileName() != null && request.profileFileUrl() != null && request.profileFileSize() != null) {
            BinaryContent profileImage = new BinaryContent(
                    request.profileFileName(),
                    request.profileFileUrl(),
                    request.profileFileSize()
            );
            binaryContentRepository.save(profileImage);
            user.updateProfile(profileImage.getId());
        }

        userRepository.save(user);

        UserStatus userStatus = new UserStatus(user.getId());
        userStatusRepository.save(userStatus);
        return toResponse(user);
    }

    @Override
    public UserResponse find(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));
        return toResponse(user);
    }

    @Override
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public UserResponse update(UUID id, UserUpdateRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("수정할 유저를 찾을 수 없습니다."));

        user.update(
                request.email(),
                request.username(),
                request.password(),
                request.statusMessage()
        );

        if (request.profileFileName() != null && request.profileFileUrl() != null) {
            BinaryContent newProfileImage = new BinaryContent(
                    request.profileFileName(),
                    request.profileFileUrl(),
                    request.profileFileSize()
            );
            binaryContentRepository.save(newProfileImage);
            user.updateProfile(newProfileImage.getId());
        }

        userRepository.save(user);
        return toResponse(user);
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

    private UserResponse toResponse(User user) {
        boolean isOnline = userStatusRepository.findByUserId(user.getId())
                .map(UserStatus::isOnline).orElse(false);

        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getStatusMessage(),
                isOnline
        );
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
}