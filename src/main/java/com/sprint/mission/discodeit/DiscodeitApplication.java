package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DiscodeitApplication {

    public static void main(String[] args) {
        // 스프링 컨테이너 가동 (이 순간 @Service, @Repository 객체들이 메모리에 생성되고 조립됨)
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        // 서비스 초기화 (개발자가 new를 쓰지 않고, 스프링 컨테이너에서 빈을 꺼내옴)
        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);

        System.out.println("====== 테스트 시작 ======\n");

        // --- [1] 데이터 등록 ---
        System.out.println("[1] 유저, 채널, 메시지 생성");
        UserResponse user1 = userService.create(new UserCreateRequest(
                "hello@discord.com",
                "개발자",
                "password",
                null, null, null));
        Channel channel1 = channelService.createPublic(new PublicChannelCreateRequest(
                "개발-잡담", "개발자와 개발 관련 이야기를 나누는 채널입니다."));

        // 생성된 유저와 채널의 ID를 활용하여 메시지 작성
        Message message1 = messageService.create(new Message(channel1.getId(),
                user1.id(), "안녕하세요, 첫 메시지입니다!"));
        System.out.println(" -> 메시지 작성 완료: " + message1.getContent() + "\n");

        // --- [2] 데이터 조회 (단건, 다건) ---
        System.out.println("[2] 데이터 조회");

        // 1. 단건 조회 테스트
        UserResponse foundUser = userService.find(user1.id());
        ChannelResponse foundChannel = channelService.find(channel1.getId());
        System.out.println(" [단건 조회]");
        System.out.println(" -> 찾은 유저 이름: " + foundUser.username());
        System.out.println(" -> 찾은 채널 이름: " + foundChannel.name() + "\n");

        // 2. 다건 조회 테스트
        System.out.println(" [다건 조회]");
        System.out.println(" -> 총 유저 수: " + userService.findAll().size());
        System.out.println(" -> 총 채널 수: " + channelService.findAllByUserId(user1.id()).size());
        System.out.println(" -> 총 메시지 수: " + messageService.readAll().size() + "\n");

        // --- [3] 데이터 수정 ---
        System.out.println("[3] 데이터 수정");

        // 1. 유저 정보 수정
        userService.update(user1.id(), new UserUpdateRequest(
                "world@discord.com",
                "백엔드개발자",
                "new_password",
                "개발중",
                null, null, null));
        // 2. 채널 정보 수정
        channelService.update(channel1.getId(), new ChannelUpdateRequest(
                "백엔드-잡담", ChannelType.PUBLIC, "설명이 수정되었습니다."));
        // 3. 메시지 내용 수정
        messageService.update(message1.getId(), "내용을 수정했습니다");
        System.out.println(" -> 유저, 채널, 메시지 수정 실행 완료");

        // --- [4] 수정된 데이터 조회 ---
        System.out.println("\n[4] 수정된 데이터 조회");

        // 1. 수정된 유저 검증
        UserResponse updatedUser = userService.find(user1.id());
        System.out.println("[유저]");
        System.out.println(" -> 변경 후 이름: " + updatedUser.username() +
                " (이메일: " + updatedUser.email() + ")");
        System.out.println(" -> 온라인 접속 여부: " + updatedUser.isOnline() + "\n");

        // 2. 수정된 채널 검증
        ChannelResponse updatedChannel = channelService.find(channel1.getId());
        System.out.println("[채널]");
        System.out.println(" -> 변경 후 이름: " + updatedChannel.name());
        System.out.println(" -> 마지막 메시지 시간: " + updatedChannel.lastMessageTime() + "\n");

        // 3. 수정된 메시지 검증
        Message updatedMessage = messageService.read(message1.getId());
        System.out.println("[메시지]");
        System.out.println(" -> 변경 후 내용: " + updatedMessage.getContent());
        System.out.println(" -> 생성 시간(createdAt): " + updatedMessage.getCreatedAt());
        System.out.println(" -> 수정 시간(updatedAt): " + updatedMessage.getUpdatedAt() + "\n");

        // --- [5] 삭제 ---
        System.out.println("[5] 데이터 삭제");
        messageService.delete(message1.getId());
        channelService.delete(channel1.getId());
        userService.delete(user1.id());
        System.out.println(" -> 유저, 채널, 메시지 삭제 실행 완료\n");

        // --- [6] 조회를 통해 삭제 확인 ---
        System.out.println("[6] 삭제 결과 검증");
        boolean userDeleted = false;
        try {
            userService.find(user1.id());
        } catch (IllegalArgumentException e) {
            userDeleted = true;
        }

        boolean channelDeleted = false;
        try {
            channelService.find(channel1.getId());
        } catch (IllegalArgumentException e) {
            channelDeleted = true;
        }

        if (messageService.read(message1.getId()) == null && userDeleted && channelDeleted) {
            System.out.println(" -> 테스트 성공: 모든 데이터가 정상적으로 삭제되었습니다.\n");
        } else {
            System.out.println(" -> 테스트 실패: 삭제되지 않은 데이터가 남아있습니다.\n");
        }

        System.out.println("====== 테스트 종료 ======");
    }
}
