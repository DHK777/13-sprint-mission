package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.UserStatusResponse;
import com.sprint.mission.discodeit.dto.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatusResponse create(UserStatusCreateRequest request) {
        if (userRepository.findById(request.userId()).isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }

        if (userStatusRepository.findByUserId(request.userId()).isPresent()) {
            throw new IllegalArgumentException("해당 유저의 상태 정보가 이미 존재합니다.");
        }

        UserStatus status = new UserStatus(request.userId());
        userStatusRepository.save(status);

        return toResponse(status);
    }

    @Override
    public UserStatusResponse find(UUID id) {
        UserStatus status = userStatusRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상태 정보를 찾을 수 없습니다."));
        return toResponse(status);
    }

    @Override
    public List<UserStatusResponse> findAll() {
        return userStatusRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public UserStatusResponse update(UUID id, UserStatusUpdateRequest request) {
        UserStatus status = userStatusRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상태 정보를 찾을 수 없습니다."));
        status.updateActivity();
        userStatusRepository.save(status);
        return toResponse(status);
    }

    @Override
    public UserStatusResponse updateByUserId(UUID userId, UserStatusUpdateRequest request) {
        UserStatus status = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저의 상태 정보를 찾을 수 없습니다."));

        status.updateActivity();
        userStatusRepository.save(status);
        return toResponse(status);
    }

    @Override
    public void delete(UUID id) {
        find(id);
        userStatusRepository.delete(id);
    }

    private UserStatusResponse toResponse(UserStatus status) {
        return new UserStatusResponse(
                status.getId(), status.getUserId(),
                status.getLastActiveAt(), status.isOnline()
        );
    }
}
