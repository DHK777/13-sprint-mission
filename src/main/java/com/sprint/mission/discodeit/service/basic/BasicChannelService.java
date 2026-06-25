package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.*;
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
  public Channel createPublicChannel(PublicChannelCreateRequest request) {
    Channel channel = new Channel(
        request.name(),
        ChannelType.PUBLIC,
        request.description()
    );
    channelRepository.save(channel);
    return channel;
  }

  @Override
  public Channel createPrivateChannel(PrivateChannelCreateRequest request) {
    Channel channel = new Channel(
        "",
        ChannelType.PRIVATE,
        ""
    );
    channelRepository.save(channel);

    if (request.participantIds() != null) {
      for (UUID userId : request.participantIds()) {
        ReadStatus readStatus = new ReadStatus(channel.getId(), userId);
        readStatusRepository.save(readStatus);
      }
    }
    return channel;
  }

  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
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
        .map(this::convertToDto)
        .toList();
  }

  @Override
  public Channel update(UUID id, PublicChannelUpdateRequest request) {
    Channel channel = channelRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("수정할 채널을 찾을 수 없습니다."));

    if (channel.getType() == ChannelType.PRIVATE) {
      throw new IllegalArgumentException("PRIVATE 채널은 수정할 수 없습니다.");
    }

    channel.update(request.newName(), channel.getType(), request.newDescription());
    channelRepository.save(channel);
    return channel;
  }

  @Override
  public void delete(UUID id) {
    channelRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("삭제할 채널을 찾을 수 없습니다."));

    messageRepository.deleteByChannelId(id);
    readStatusRepository.deleteByChannelId(id);
    channelRepository.delete(id);
  }

  private ChannelDto convertToDto(Channel channel) {
    List<Message> messages = messageRepository.findByChannelId(channel.getId());
    Instant lastMessageAt = null;
    if (!messages.isEmpty()) {
      lastMessageAt = messages.stream()
          .map(Message::getCreatedAt)
          .max(Instant::compareTo)
          .orElse(null);
    }

    List<UUID> participantIds = null;
    if (channel.getType() == ChannelType.PRIVATE) {
      participantIds = readStatusRepository.findByChannelId(channel.getId()).stream()
          .map(ReadStatus::getUserId)
          .toList();
    }

    return new ChannelDto(
        channel.getId(),
        channel.getType(),
        channel.getName(),
        channel.getDescription(),
        participantIds,
        lastMessageAt
    );
  }
}