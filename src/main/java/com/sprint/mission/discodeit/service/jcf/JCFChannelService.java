package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data;

    public JCFChannelService() {
        this.data = new HashMap<>();
    }

    @Override
    public Channel create(Channel channel) {
        data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Channel read(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Channel> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Channel update(UUID id, String name, ChannelType type, String description) {
        Channel channel = data.get(id);
        if (channel != null) {
            channel.update(name, type, description);
        }
        return channel;
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }
}