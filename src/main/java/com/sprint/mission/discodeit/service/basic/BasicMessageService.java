package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentStorage binaryContentStorage;

  @Override
  public Message create(UUID channelId, UUID authorId, String content,
      List<MultipartFile> attachments) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채널입니다."));
    User author = userRepository.findById(authorId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

    Message message = new Message(channel, author, content);

    if (attachments != null) {
      for (MultipartFile file : attachments) {
        if (file.isEmpty()) {
          continue;
        }

        String fileName = file.getOriginalFilename();
        long fileSize = file.getSize();
        String contentType = file.getContentType();
        String fileUrl = "/api/binaryContents/";

        BinaryContent attachment = new BinaryContent(fileName, fileUrl, fileSize, contentType);
        message.addAttachment(attachment);
      }
    }

    messageRepository.save(message);

    if (attachments != null) {
      List<BinaryContent> savedAttachments = message.getAttachments();
      int index = 0;

      for (MultipartFile file : attachments) {
        if (file.isEmpty()) {
          continue;
        }

        try {
          binaryContentStorage.put(savedAttachments.get(index).getId(), file.getBytes());
          index++;
        } catch (Exception e) {
          throw new RuntimeException("메시지 첨부파일 저장 중 오류 발생", e);
        }
      }
    }

    return message;
  }

  @Override
  @Transactional(readOnly = true)
  public Message read(UUID id) {
    return messageRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("메시지를 찾을 수 없습니다."));
  }

  @Override
  @Transactional(readOnly = true)
  public Slice<Message> findAllByChannelId(UUID channelId, int page) {
    Pageable pageable = PageRequest.of(page, 50, Sort.by(Sort.Direction.DESC, "createdAt"));

    return messageRepository.findByChannelId(channelId, pageable);
  }

  @Override
  public Message update(UUID id, String newContent) {
    Message message = messageRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("수정할 메시지를 찾을 수 없습니다."));
    message.update(newContent);
    return message;
  }

  @Override
  public void delete(UUID id) {
    Message message = messageRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("삭제할 메시지를 찾을 수 없습니다."));
    messageRepository.delete(message);
  }
}