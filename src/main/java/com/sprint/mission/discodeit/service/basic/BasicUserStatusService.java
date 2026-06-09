package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserStatusCreateRequest;
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
    public UserStatus create(UserStatusCreateRequest request) {
        if (userRepository.findById(request.userId()) == null) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }

        if (userStatusRepository.findByUserId(request.userId()) != null) {
            throw new IllegalArgumentException("해당 유저의 상태 정보가 이미 존재합니다.");
        }

        UserStatus userStatus = new UserStatus(request.userId());
        userStatusRepository.save(userStatus);

        return userStatus;
    }

    @Override
    public UserStatus find(UUID id) {
        UserStatus status = userStatusRepository.findById(id);
        if (status == null) {
            throw new IllegalArgumentException("상태 정보를 찾을 수 없습니다.");
        }
        return status;
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll();
    }

    @Override
    public UserStatus update(UUID id, UserStatusUpdateRequest request) {
        UserStatus status = find(id);
        status.updateActivity();
        userStatusRepository.save(status);
        return status;
    }

    @Override
    public UserStatus updateByUserId(UUID userId, UserStatusUpdateRequest request) {
        UserStatus status = userStatusRepository.findByUserId(userId);
        if (status == null) {
            throw new IllegalArgumentException("해당 유저의 상태 정보를 찾을 수 없습니다.");
        }

        status.updateActivity();
        userStatusRepository.save(status);
        return status;
    }

    @Override
    public void delete(UUID id) {
        find(id);
        userStatusRepository.delete(id);
    }
}
