package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "messages")
@Getter
public class Message extends BaseUpdatableEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "channel_id", nullable = false)
  private Channel channel;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id")
  private User author;

  @Column(columnDefinition = "TEXT")
  private String content;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinTable(
      name = "message_attachments",
      joinColumns = @JoinColumn(name = "message_id"),
      inverseJoinColumns = @JoinColumn(name = "attachment_id")
  )
  private List<BinaryContent> attachments = new ArrayList<>();

  protected Message() {
  }

  public Message(Channel channel, User author, String content) {
    this.channel = channel;
    this.author = author;
    this.content = content;
  }

  public void update(String content) {
    this.content = content;
  }

  public void addAttachment(BinaryContent attachment) {
    this.attachments.add(attachment);
  }
}