package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import java.util.concurrent.locks.ReentrantLock;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.*;
import java.util.*;

@Repository
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "file")
public class FileMessageRepository implements MessageRepository {

  private static final String FILE_PATH = "messages.dat";
  private final Map<UUID, Message> store;
  private final FileLockProvider fileLockProvider;

  public FileMessageRepository(FileLockProvider fileLockProvider) {
    this.fileLockProvider = fileLockProvider;
    this.store = loadData();
  }

  @SuppressWarnings("unchecked")
  private Map<UUID, Message> loadData() {
    Path filePath = Paths.get(FILE_PATH);
    ReentrantLock lock = fileLockProvider.getLock(filePath);

    lock.lock();
    try {
      if (!Files.exists(filePath)) {
        return new HashMap<>();
      }
      try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
        return (Map<UUID, Message>) ois.readObject();
      } catch (IOException | ClassNotFoundException e) {
        return new HashMap<>();
      }
    } finally {
      lock.unlock();
    }
  }

  private void saveData(Map<UUID, Message> data) {
    Path filePath = Paths.get(FILE_PATH);
    ReentrantLock lock = fileLockProvider.getLock(filePath);

    lock.lock();
    try {
      try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
        oos.writeObject(data);
      } catch (IOException e) {
        throw new RuntimeException("메시지 파일 저장 중 오류 발생", e);
      }
    } finally {
      lock.unlock();
    }
  }

  @Override
  public void save(Message message) {
    store.put(message.getId(), message);
    saveData(store);
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
    saveData(store);
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
    saveData(store);
  }
}