package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.util.concurrent.locks.ReentrantLock;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.*;
import java.util.*;

@Repository
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "file")
public class FileReadStatusRepository implements ReadStatusRepository {

  private static final String FILE_PATH_STR = "read_statuses.dat";
  private final Map<UUID, ReadStatus> store;
  private final FileLockProvider fileLockProvider;

  public FileReadStatusRepository(FileLockProvider fileLockProvider) {
    this.fileLockProvider = fileLockProvider;
    this.store = loadData();
  }

  @SuppressWarnings("unchecked")
  private Map<UUID, ReadStatus> loadData() {
    Path filePath = Paths.get(FILE_PATH_STR);
    ReentrantLock lock = fileLockProvider.getLock(filePath);

    lock.lock();
    try {
        if (!Files.exists(filePath)) {
            return new HashMap<>();
        }
      try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
        return (Map<UUID, ReadStatus>) ois.readObject();
      } catch (IOException | ClassNotFoundException e) {
        return new HashMap<>();
      }
    } finally {
      lock.unlock();
    }
  }

  private void saveData(Map<UUID, ReadStatus> data) {
    Path filePath = Paths.get(FILE_PATH_STR);
    ReentrantLock lock = fileLockProvider.getLock(filePath);

    lock.lock();
    try {
      try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
        oos.writeObject(data);
      } catch (IOException e) {
        throw new RuntimeException("읽음 상태 파일 저장 중 오류 발생", e);
      }
    } finally {
      lock.unlock();
    }
  }

  @Override
  public void save(ReadStatus readStatus) {
    store.put(readStatus.getId(), readStatus);
    saveData(store);
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
    saveData(store);
  }

  @Override
  public Optional<ReadStatus> findById(UUID id) {
    return Optional.ofNullable(store.get(id));
  }

  @Override
  public void delete(UUID id) {
    store.remove(id);
    saveData(store);
  }

  @Override
  public Optional<ReadStatus> findByChannelIdAndUserId(UUID channelId, UUID userId) {
    return store.values().stream()
        .filter(rs -> rs.getChannelId().equals(channelId) && rs.getUserId().equals(userId))
        .findFirst();
  }
}