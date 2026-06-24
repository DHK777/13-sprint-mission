package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.AttachmentRequest;
import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageResponse;
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

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public MessageResponse create(MessageCreateRequest request) {
        if (channelRepository.findById(request.channelId()).isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다.");
        }
        if (userRepository.findById(request.senderId()).isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }

        Message message = new Message(request.channelId(), request.senderId(), request.content());

        if (request.attachments() != null && !request.attachments().isEmpty()) {
            for (AttachmentRequest att : request.attachments()) {
                BinaryContent file = new BinaryContent(att.fileName(), att.fileUrl(), att.fileSize());
                binaryContentRepository.save(file);
                message.addAttachmentId(file.getId());
            }
        }
        messageRepository.save(message);
        return toResponse(message);
    }

    @Override
    public MessageResponse read(UUID id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("메시지를 찾을 수 없습니다."));
        return toResponse(message);
    }

    @Override
    public List<MessageResponse> findAllByChannelId(UUID channelId) {
        return messageRepository.findByChannelId(channelId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public MessageResponse update(UUID id, MessageUpdateRequest request) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("수정할 메시지를 찾을 수 없습니다."));
        message.update(request.content());
        messageRepository.save(message);
        return toResponse(message);
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

    private MessageResponse toResponse(Message message) {
        return new MessageResponse(
                message.getId(), message.getChannelId(), message.getAuthorId(),
                message.getContent(), message.getAttachmentIds(),
                message.getCreatedAt(), message.getUpdatedAt()
        );
    }
}