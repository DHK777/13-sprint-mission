package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.UUID;

public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;

    public BasicMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message create(Message message) {
        messageRepository.save(message);
        return message;
    }

    @Override
    public Message read(UUID id) {
        return messageRepository.findById(id);
    }

    @Override
    public List<Message> readAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message update(UUID id, String content) {
        Message message = messageRepository.findById(id);
        if (message != null) {
            message.update(content);
            messageRepository.save(message);
        }
        return message;
    }

    @Override
    public void delete(UUID id) {
        messageRepository.delete(id);
    }
}