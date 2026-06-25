package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;

  @Override
  public Message create(MessageCreateRequest request, List<MultipartFile> attachments) {
    if (channelRepository.findById(request.channelId()).isEmpty()) {
      throw new IllegalArgumentException("존재하지 않는 채널입니다.");
    }
    if (userRepository.findById(request.authorId()).isEmpty()) {
      throw new IllegalArgumentException("존재하지 않는 유저입니다.");
    }

    Message message = new Message(request.channelId(), request.authorId(), request.content());

    if (attachments != null && !attachments.isEmpty()) {
      for (MultipartFile file : attachments) {
        if (!file.isEmpty()) {
          String fileName = file.getOriginalFilename();
          long fileSize = file.getSize();
          String fileUrl = "/files/" + fileName;

          BinaryContent attachment = new BinaryContent(fileName, fileUrl, fileSize);
          binaryContentRepository.save(attachment);
          message.addAttachmentId(attachment.getId());
        }
      }
    }
    messageRepository.save(message);
    return message;
  }

  @Override
  public Message read(UUID id) {
    return messageRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("메시지를 찾을 수 없습니다."));
  }

  @Override
  public List<Message> findAllByChannelId(UUID channelId) {
    return messageRepository.findByChannelId(channelId);
  }

  @Override
  public Message update(UUID id, MessageUpdateRequest request) {
    Message message = messageRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("수정할 메시지를 찾을 수 없습니다."));
    message.update(request.newContent());
    messageRepository.save(message);
    return message;
  }

  @Override
  public void delete(UUID id) {
    Message message = messageRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("삭제할 메시지를 찾을 수 없습니다."));
    for (UUID fileId : message.getAttachmentIds()) {
      binaryContentRepository.deleteById(fileId);
    }
    messageRepository.delete(id);
  }
}