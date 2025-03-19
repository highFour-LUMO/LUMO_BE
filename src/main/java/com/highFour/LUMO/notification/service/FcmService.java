package com.highFour.LUMO.notification.service;

import static com.highFour.LUMO.common.exceptionType.MemberExceptionType.*;
import static com.highFour.LUMO.common.exceptionType.NotificationExceptionType.*;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;
import com.highFour.LUMO.common.exception.BaseCustomException;
import com.highFour.LUMO.common.exceptionType.MemberExceptionType;
import com.highFour.LUMO.member.entity.Member;
import com.highFour.LUMO.member.repository.MemberRepository;
import com.highFour.LUMO.notification.dto.FcmTokenSaveRequest;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class FcmService {
	private final MemberRepository memberRepository;

	// fcm 토큰 저장
	@Transactional
	public void saveFcmToken(FcmTokenSaveRequest fcmTokenSaveRequest) {
		String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		Member member = memberRepository.findByEmail(memberEmail)
			.orElseThrow(() -> new BaseCustomException(MEMBER_NOT_FOUND));

		member.updateFcmToken(fcmTokenSaveRequest.fcmToken());
	}

	// 알림 전송
	public void sendNotification() {
		String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		Member member = memberRepository.findByEmail(memberEmail)
			.orElseThrow(() -> new BaseCustomException(MEMBER_NOT_FOUND));

		String token = member.getFcmToken();
		if(token == null || token.isEmpty()) {
			throw new BaseCustomException(FCM_TOKEN_NOT_FOUND);
		}

		Message message = Message.builder()
			.setWebpushConfig(WebpushConfig.builder()
				.setNotification(WebpushNotification.builder()
					.setTitle("테스트 알림 제목")
					.setBody("테스트 알림 내용")
					.build())
				.build())
			.setToken(token)
			.build();

		try {
			FirebaseMessaging.getInstance().sendAsync(message);
		} catch(Exception e) {
			throw new BaseCustomException(FCM_SEND_FAIL);
		}
	}
}