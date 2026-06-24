package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "jcf", matchIfMissing = true)
public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> store = new HashMap<>();

    @Override
    public void save(Message message) {
        store.put(message.getId(), message);
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void delete(UUID id) {
        store.remove(id);
    }

    @Override
    public List<Message> findByChannelId(UUID channelId) {
        return store.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        store.values().removeIf(message -> message.getChannelId().equals(channelId));
    }
}