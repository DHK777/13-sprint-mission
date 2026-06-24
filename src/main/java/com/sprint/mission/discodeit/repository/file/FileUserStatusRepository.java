package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.*;
import java.util.*;

@Repository
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "file")
public class FileUserStatusRepository implements UserStatusRepository {
    private static final String FILE_PATH_STR = "user_statuses.dat";
    private final Map<UUID, UserStatus> store;

    public FileUserStatusRepository() {
        this.store = loadData();
    }

    @SuppressWarnings("unchecked")
    private Map<UUID, UserStatus> loadData() {
        Path filePath = Paths.get(FILE_PATH_STR);
        if (!Files.exists(filePath)) return new HashMap<>();
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
            return (Map<UUID, UserStatus>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();
        }
    }

    private void saveData(Map<UUID, UserStatus> data) {
        Path filePath = Paths.get(FILE_PATH_STR);
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("유저 상태 파일 저장 중 오류 발생", e);
        }
    }

    @Override
    public void save(UserStatus userStatus) {
        store.put(userStatus.getId(), userStatus);
        saveData(store);
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return store.values().stream()
                .filter(status -> status.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public void deleteByUserId(UUID userId) {
        store.values().removeIf(status -> status.getUserId().equals(userId));
        saveData(store);
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<UserStatus> findAll() {
        return store.values().stream().toList();
    }

    @Override
    public void delete(UUID id) {
        store.remove(id);
        saveData(store);
    }
}