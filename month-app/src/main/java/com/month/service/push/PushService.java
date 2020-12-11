package com.month.service.push;

import com.google.firebase.messaging.*;
import com.month.domain.challenge.Challenge;
import com.month.domain.member.MemberRepository;
import com.month.service.push.dto.request.PushRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class PushService {

    private final MemberRepository memberRepository;

    public void send(PushRequest pushRequest) {
        Assert.notNull(pushRequest, "'pushRequest' must not be null");

        Notification notification = Notification.builder()
                .setTitle(pushRequest.getTitle())
                .setBody(pushRequest.getBody())
                .build();

        List<Message> messages = pushRequest.getDeviceTokens().stream()
                .map(token -> Message.builder()
                        .setNotification(notification)
                        .setToken(token)
                        .build())
                .collect(Collectors.toList());

        BatchResponse batchResponse = null;
        try {
            batchResponse = FirebaseMessaging.getInstance().sendAll(messages);
        } catch (FirebaseMessagingException ex) {
            throw new IllegalArgumentException(String.format("파이어베이스를 사용하여 푸시메시지를 보내지 못했습니다. (%s)", ex));
        }
    }

    public void sendParticipators(Challenge challenge) {
        List<String> deviceTokens = challenge.getMemberIds()
                .stream()
                .map(id -> memberRepository.findById(id).get().getDeviceToken())
                .collect(Collectors.toList());

        PushRequest pushRequest = PushRequest.of(deviceTokens, "젤로 알림", "새로운 초대가 도착했습니다.");
        send(pushRequest);
    }

}
