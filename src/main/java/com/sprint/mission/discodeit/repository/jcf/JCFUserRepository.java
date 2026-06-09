package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFUserRepository implements UserRepository {

    private final Map<UUID, User> store = new HashMap<>();

    @Override
    public void save(User user) {
        store.put(user.getId(), user);
    }

    @Override
    public User findById(UUID id) {
        return store.get(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void delete(UUID id) {
        store.remove(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        // 같은 이름이 하나라도 있으면 true 없으면 false
        return store.values().stream()
                .anyMatch(user -> user.getUsername().equals(username));
    }

    @Override
    public boolean existsByEmail(String email) {
        // 같은 이메일이 하나라도 있으면 true 없으면 false
        return store.values().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public User findByUsername(String username) {
        // 맵을 싹 뒤져서 이름이 똑같은 유저를 찾아서 뱉음 (없으면 null)
        return store.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }
}