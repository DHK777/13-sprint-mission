package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelResponse;
import com.sprint.mission.discodeit.dto.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    @Override
    public ChannelResponse createPublic(PublicChannelCreateRequest request) {
        Channel channel = new Channel(
                request.name(),
                ChannelType.PUBLIC,
                request.description()
        );
        channelRepository.save(channel);
        return find(channel.getId());
    }

    @Override
    public ChannelResponse createPrivate(PrivateChannelCreateRequest request) {
        Channel channel = new Channel(
                "",
                ChannelType.PRIVATE,
                ""
        );
        channelRepository.save(channel);

        if (request.userIds() != null) {
            for (UUID userId : request.userIds()) {
                ReadStatus readStatus = new ReadStatus(channel.getId(), userId);
                readStatusRepository.save(readStatus);
            }
        }
        return find(channel.getId());
    }

    @Override
    public ChannelResponse find(UUID id) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다."));

        List<Message> messages = messageRepository.findByChannelId(id);
        Instant lastMessageTime = null;
        if (!messages.isEmpty()) {
            lastMessageTime = messages.stream()
                    .map(Message::getCreatedAt)
                    .max(Instant::compareTo)
                    .orElse(null);
        }

        List<UUID> userIds = null;
        if (channel.getType() == ChannelType.PRIVATE) {
            userIds = readStatusRepository.findByChannelId(id).stream()
                    .map(ReadStatus::getUserId)
                    .toList();
        }

        return new ChannelResponse(
                channel.getId(),
                channel.getName(),
                channel.getType(),
                channel.getDescription(),
                lastMessageTime,
                userIds
        );
    }

    @Override
    public List<ChannelResponse> findAllByUserId(UUID userId) {
        List<Channel> allChannels = channelRepository.findAll();

        List<Channel> allowedChannels = allChannels.stream().filter(channel -> {
            if (channel.getType() == ChannelType.PUBLIC) {
                return true;
            } else {
                List<ReadStatus> myStatuses = readStatusRepository.findByUserId(userId);
                return myStatuses.stream()
                        .anyMatch(rs -> rs.getChannelId().equals(channel.getId()));
            }
        }).toList();

        return allowedChannels.stream()
                .map(channel -> find(channel.getId()))
                .toList();
    }

    @Override
    public ChannelResponse update(UUID id, ChannelUpdateRequest request) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("수정할 채널을 찾을 수 없습니다."));

        if (channel.getType() == ChannelType.PRIVATE) {
            throw new IllegalArgumentException("PRIVATE 채널은 수정할 수 없습니다.");
        }

        channel.update(request.name(), request.type(), request.description());
        channelRepository.save(channel);
        return find(id);
    }

    @Override
    public void delete(UUID id) {
        channelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("삭제할 채널을 찾을 수 없습니다."));

        messageRepository.deleteByChannelId(id);
        readStatusRepository.deleteByChannelId(id);
        channelRepository.delete(id);
    }
}