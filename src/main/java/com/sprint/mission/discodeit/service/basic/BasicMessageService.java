package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.FileUploadDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final MessageMapper messageMapper;

  @Override
  @Transactional
  public MessageDto create(UUID channelId, UUID authorId, String content,
      List<FileUploadDto> files) {

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채널입니다."));
    User author = userRepository.findById(authorId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

    Message message = new Message(channel, author, content);

    if (files != null) {
      for (FileUploadDto file : files) {
        if (file.bytes() == null || file.bytes().length == 0) {
          continue;
        }

        String fileName = file.fileName();
        long fileSize = file.size();
        String contentType = file.contentType();
        String fileUrl = "/api/binaryContents/";

        BinaryContent attachment = new BinaryContent(fileName, fileUrl, fileSize, contentType);
        message.addAttachment(attachment);
      }
    }

    messageRepository.save(message);

    if (files != null) {
      List<BinaryContent> savedAttachments = message.getAttachments();
      int index = 0;

      for (FileUploadDto file : files) {
        if (file.bytes() == null || file.bytes().length == 0) {
          continue;
        }

        try {
          binaryContentStorage.put(savedAttachments.get(index).getId(), file.bytes());
          index++;
        } catch (Exception e) {
          throw new RuntimeException("메시지 첨부파일 저장 중 오류 발생", e);
        }
      }
    }

    return messageMapper.toDto(message);
  }

  @Override
  public MessageDto read(UUID id) {
    Message message = messageRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("메시지를 찾을 수 없습니다."));

    return messageMapper.toDto(message);
  }

  @Override
  public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant cursor,
      Pageable pageable) {
    Slice<Message> slice;

    if (cursor == null) {
      slice = messageRepository.findByChannelIdOrderByCreatedAtDesc(channelId, pageable);
    } else {
      slice = messageRepository.findByChannelIdAndCreatedAtLessThanOrderByCreatedAtDesc(channelId,
          cursor, pageable);
    }

    List<MessageDto> content = slice.getContent().stream()
        .map(messageMapper::toDto)
        .toList();

    Instant nextCursor = null;
    if (slice.hasNext() && !slice.getContent().isEmpty()) {
      nextCursor = slice.getContent().get(slice.getNumberOfElements() - 1).getCreatedAt();
    }

    return new PageResponse<>(content, nextCursor, slice.getSize(), slice.hasNext(), null);
  }

  @Override
  @Transactional
  public MessageDto update(UUID id, String newContent) {
    Message message = messageRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("수정할 메시지를 찾을 수 없습니다."));

    message.update(newContent);

    return messageMapper.toDto(message);
  }

  @Override
  @Transactional
  public void delete(UUID id) {
    Message message = messageRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("삭제할 메시지를 찾을 수 없습니다."));

    messageRepository.delete(message);
  }
}