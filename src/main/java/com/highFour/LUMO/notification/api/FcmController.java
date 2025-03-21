package com.highFour.LUMO.notification.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.highFour.LUMO.notification.dto.FcmTokenSaveRequest;
import com.highFour.LUMO.notification.service.FcmService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/fcm")
@RestController
public class FcmController {
	private final FcmService fcmService;

	// fcm 토큰 저장
	@PostMapping("/token")
	public ResponseEntity<?> saveFcmToken(@RequestBody FcmTokenSaveRequest fcmTokenSaveRequest) {
		fcmService.saveFcmToken(fcmTokenSaveRequest);
		return new ResponseEntity<>("fcm token 저장을 성공하였습니다.", HttpStatus.OK);
	}

	// 테스트용 알림 전송
	@PostMapping("/notice")
	public ResponseEntity<?> sendNotification() {
		fcmService.sendNotification();
		return new ResponseEntity<>("fcm 알림 전송을 성공하였습니다.", HttpStatus.OK);
	}
}
