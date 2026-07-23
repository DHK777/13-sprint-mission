package com.sprint.mission.discodeit.storage;

import java.io.InputStream;
import java.util.UUID;
import org.springframework.core.io.Resource;

public interface BinaryContentStorage {

  UUID put(UUID id, byte[] data);

  InputStream get(UUID id);

  Resource download(UUID id);
}
