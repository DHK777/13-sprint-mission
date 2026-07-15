package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;

  @Override
  public Channel createPublic(String name, String description) {
    Channel channel = new Channel(name, ChannelType.PUBLIC, description);
    channelRepository.save(channel);
    return channel;
  }

  @Override
  public Channel createPrivate(List<UUID> participantIds) {
    Channel channel = new Channel("", ChannelType.PRIVATE, "");
    channelRepository.save(channel);

    if (participantIds != null) {
      for (UUID userId : participantIds) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        ReadStatus readStatus = new ReadStatus(user, channel, channel.getCreatedAt());
        readStatusRepository.save(readStatus);
      }
    }
    return channel;
  }

  @Override
  @Transactional(readOnly = true)
  public Channel find(UUID id) {
    return channelRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다."));
  }

  @Override
  @Transactional(readOnly = true)
  public List<Channel> findAllByUserId(UUID userId) {
    List<Channel> allChannels = channelRepository.findAll();

    return allChannels.stream().filter(channel -> {
      if (channel.getType() == ChannelType.PUBLIC) {
        return true;
      } else {
        List<ReadStatus> myStatuses = readStatusRepository.findByUserId(userId);
        return myStatuses.stream()
            .anyMatch(rs -> rs.getChannel().getId().equals(channel.getId()));
      }
    }).toList();
  }

  @Override
  public Channel update(UUID id, String name, String description) {
    Channel channel = channelRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("수정할 채널을 찾을 수 없습니다."));

    if (channel.getType() == ChannelType.PRIVATE) {
      throw new IllegalArgumentException("PRIVATE 채널은 수정할 수 없습니다.");
    }

    channel.update(name, channel.getType(), description);
    return channel;
  }

  @Override
  public void delete(UUID id) {
    Channel channel = channelRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("삭제할 채널을 찾을 수 없습니다."));

    messageRepository.deleteByChannelId(id);
    readStatusRepository.deleteByChannelId(id);
    channelRepository.delete(channel);
  }
}