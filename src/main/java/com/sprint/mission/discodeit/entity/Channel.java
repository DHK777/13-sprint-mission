package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "channels")
@Getter
public class Channel extends BaseUpdatableEntity {

  @Column(length = 100)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 10)
  private ChannelType type;

  @Column(length = 500)
  private String description;

  protected Channel() {
  }

  public Channel(String name, ChannelType type, String description) {
    this.name = name;
    this.type = type;
    this.description = description;
  }

  public void update(String name, ChannelType type, String description) {
    this.name = name;
    this.type = type;
    this.description = description;
  }
}