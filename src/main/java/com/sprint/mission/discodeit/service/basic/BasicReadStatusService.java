package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public ReadStatus create(ReadStatusCreateRequest request) {
        if (channelRepository.findById(request.channelId()).isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다.");
        }
        if (userRepository.findById(request.userId()).isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }

        if (readStatusRepository.findByChannelIdAndUserId(request.channelId(), request.userId()).isPresent()) {
            throw new IllegalArgumentException("해당 유저는 이미 이 채널의 읽음 상태를 가지고 있습니다.");
        }

        ReadStatus readStatus = new ReadStatus(request.channelId(), request.userId());
        readStatusRepository.save(readStatus);
        return readStatus;
    }

    @Override
    public ReadStatus find(UUID id) {
        return readStatusRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상태창을 찾을 수 없습니다."));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findByUserId(userId);
    }

    @Override
    public ReadStatus update(UUID id, ReadStatusUpdateRequest request) {
        ReadStatus readStatus = find(id);
        readStatus.updateLastReadAt();
        readStatusRepository.save(readStatus);
        return readStatus;
    }

    @Override
    public void delete(UUID id) {
        find(id);
        readStatusRepository.delete(id);
    }
}
