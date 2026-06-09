package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class JCFUserStatusRepository implements UserStatusRepository {
    private final Map<UUID, UserStatus> store = new HashMap<>();

    @Override
    public void save(UserStatus userStatus) {
        store.put(userStatus.getId(), userStatus);
    }

    @Override
    public UserStatus findByUserId(UUID userId) {
        return store.values().stream()
                .filter(status -> status.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        store.values().removeIf(status -> status.getUserId().equals(userId));
    }

    @Override
    public UserStatus findById(UUID id) {
        return store.get(id);
    }

    @Override
    public List<UserStatus> findAll() {
        return store.values().stream().toList();
    }

    @Override
    public void delete(UUID id) {
        store.remove(id);
    }
}