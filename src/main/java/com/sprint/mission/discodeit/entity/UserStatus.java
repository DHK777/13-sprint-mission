package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.Duration;
import java.time.Instant;

@Entity
@Table(name = "user_statuses")
@Getter
public class UserStatus extends BaseUpdatableEntity {

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false, unique = true)
  private User user;

  @Column(name = "last_active_at")
  private Instant lastActiveAt;

  protected UserStatus() {
  }

  public UserStatus(User user) {
    this.user = user;
    this.lastActiveAt = Instant.now();
  }

  public void updateActivity() {
    this.lastActiveAt = Instant.now();
  }

  public boolean isOnline() {
    Instant fiveMinutesAgo = Instant.now().minus(Duration.ofMinutes(5));
    return this.lastActiveAt != null && this.lastActiveAt.isAfter(fiveMinutesAgo);
  }
}