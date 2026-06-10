package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFReadStatusRepository implements ReadStatusRepository {
    private final Map<UUID, ReadStatus> store = new HashMap<>();

    @Override
    public void save(ReadStatus readStatus) {
        store.put(readStatus.getId(), readStatus);
    }

    @Override
    public List<ReadStatus> findByChannelId(UUID channelId) {
        return store.values().stream()
                .filter(rs -> rs.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public List<ReadStatus> findByUserId(UUID userId) {
        return store.values().stream()
                .filter(rs -> rs.getUserId().equals(userId))
                .toList();
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        store.values().removeIf(rs -> rs.getChannelId().equals(channelId));
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public void delete(UUID id) {
        store.remove(id);
    }

    @Override
    public Optional<ReadStatus> findByChannelIdAndUserId(UUID channelId, UUID userId) {
        return store.values().stream()
                .filter(rs -> rs.getChannelId().equals(channelId) && rs.getUserId().equals(userId))
                .findFirst();
    }
}