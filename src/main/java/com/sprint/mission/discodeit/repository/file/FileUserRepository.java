package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.concurrent.locks.ReentrantLock;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.*;
import java.util.*;

@Repository
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "file")
public class FileUserRepository implements UserRepository {

  private static final String FILE_PATH_STR = "users.dat";

  private final Map<UUID, User> store;

  private final FileLockProvider fileLockProvider;

  public FileUserRepository(FileLockProvider fileLockProvider) {
    this.fileLockProvider = fileLockProvider;
    this.store = loadData();
  }

  @SuppressWarnings("unchecked")
  private Map<UUID, User> loadData() {
    Path filePath = Paths.get(FILE_PATH_STR);
    ReentrantLock lock = fileLockProvider.getLock(filePath);

    lock.lock();
    try {
      if (!Files.exists(filePath)) {
        return new HashMap<>();
      }
      try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
        return (Map<UUID, User>) ois.readObject();
      } catch (IOException | ClassNotFoundException e) {
        return new HashMap<>();
      }
    } finally {
      lock.unlock();
    }
  }

  private void saveData(Map<UUID, User> data) {
    Path filePath = Paths.get(FILE_PATH_STR);
    ReentrantLock lock = fileLockProvider.getLock(filePath);

    lock.lock();
    try {
      try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
        oos.writeObject(data);
      } catch (IOException e) {
        throw new RuntimeException("유저 파일 저장 중 오류 발생", e);
      }
    } finally {
      lock.unlock();
    }
  }

  @Override
  public void save(User user) {
    store.put(user.getId(), user);
    saveData(store);
  }

  @Override
  public Optional<User> findById(UUID id) {
    return Optional.ofNullable(store.get(id));
  }

  @Override
  public List<User> findAll() {
    return new ArrayList<>(store.values());
  }

  @Override
  public void delete(UUID id) {
    store.remove(id);
    saveData(store);
  }

  @Override
  public boolean existsByUsername(String username) {
    return store.values().stream().anyMatch(user -> user.getUsername().equals(username));
  }

  @Override
  public boolean existsByEmail(String email) {
    return store.values().stream().anyMatch(user -> user.getEmail().equals(email));
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return store.values().stream()
        .filter(user -> user.getUsername().equals(username))
        .findFirst();
  }
}