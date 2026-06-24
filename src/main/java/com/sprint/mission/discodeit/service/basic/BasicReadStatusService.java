package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.ReadStatusResponse;
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
    public ReadStatusResponse create(ReadStatusCreateRequest request) {
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
        return toResponse(readStatus);
    }

    @Override
    public ReadStatusResponse find(UUID id) {
        ReadStatus readStatus = readStatusRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상태창을 찾을 수 없습니다."));
        return toResponse(readStatus);
    }

    public List<ReadStatusResponse> findAllByUserId(UUID userId) {
        return readStatusRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ReadStatusResponse update(UUID id, ReadStatusUpdateRequest request) {
        ReadStatus readStatus = readStatusRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상태창을 찾을 수 없습니다."));
        readStatus.updateLastReadAt();
        readStatusRepository.save(readStatus);
        return toResponse(readStatus);
    }

    @Override
    public void delete(UUID id) {
        find(id);
        readStatusRepository.delete(id);
    }

    private ReadStatusResponse toResponse(ReadStatus readStatus) {
        return new ReadStatusResponse(
                readStatus.getId(), readStatus.getChannelId(),
                readStatus.getUserId(), readStatus.getLastReadAt()
        );
    }
}
