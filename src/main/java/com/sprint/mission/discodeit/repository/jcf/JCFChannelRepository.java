package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

//@Repository
public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> store = new HashMap<>();

    @Override
    public void save(Channel channel) {
        store.put(channel.getId(), channel);
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void delete(UUID id) {
        store.remove(id);
    }
}