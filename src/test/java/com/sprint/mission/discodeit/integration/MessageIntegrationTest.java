package com.sprint.mission.discodeit.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class MessageIntegrationTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private ChannelRepository channelRepository;
  @Autowired
  private MessageRepository messageRepository;

  private User savedUser;
  private Channel savedChannel;

  @BeforeEach
  void setUp() {
    savedUser = userRepository.save(new User("author@test.com", "author", "pass"));
    savedChannel = channelRepository.save(new Channel("general", ChannelType.PUBLIC, "desc"));
  }

  @Test
  @DisplayName("통합: 메시지 생성 - 성공")
  void createMessage_Success() throws Exception {
    MessageCreateRequest requestDto = new MessageCreateRequest(savedChannel.getId(),
        savedUser.getId(), "Hello Integration!");
    MockMultipartFile requestPart = new MockMultipartFile(
        "messageCreateRequest", "", MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(requestDto)
    );

    mockMvc.perform(multipart("/api/messages")
            .file(requestPart)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.content").value("Hello Integration!"));
  }

  @Test
  @DisplayName("통합: 특정 채널의 메시지 목록 조회 - 성공")
  void getMessagesByChannelId_Success() throws Exception {
    messageRepository.save(new Message(savedChannel, savedUser, "Msg 1"));
    messageRepository.save(new Message(savedChannel, savedUser, "Msg 2"));

    mockMvc.perform(get("/api/messages")
            .param("channelId", savedChannel.getId().toString()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(2));
  }
}