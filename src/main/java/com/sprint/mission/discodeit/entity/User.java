package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "users")
@Getter
public class User extends BaseUpdatableEntity {

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "profile_id")
  private BinaryContent profile;

  @Column(nullable = false, unique = true, length = 100)
  private String email;

  @Column(nullable = false, unique = true, length = 50)
  private String username;

  @Column(nullable = false, length = 60)
  private String password;

  @Transient
  private String statusMessage;

  protected User() {
  }

  public User(String email, String username, String password) {
    this.email = email;
    this.username = username;
    this.password = password;
    this.statusMessage = "";
  }

  public void update(String email, String username, String password, String statusMessage) {
    this.email = email;
    this.username = username;
    this.password = password;
    this.statusMessage = statusMessage;
  }

  public void updateProfile(BinaryContent profile) {
    this.profile = profile;
  }
}