package com.highFour.LUMO.common.config;
import static com.highFour.LUMO.common.exceptionType.NotificationExceptionType.*;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.highFour.LUMO.common.exception.BaseCustomException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.IOException;

@Slf4j
@Configuration
public class FcmConfig {
	@Value("${fcm.secret-file}")
	private String secretFileName;

	@PostConstruct
	public void initialize() {
		try {
			GoogleCredentials googleCredentials = GoogleCredentials
				.fromStream(new ClassPathResource(secretFileName).getInputStream());
			FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(googleCredentials)
				.build();
			FirebaseApp.initializeApp(options);
		} catch(FileNotFoundException e) {
			throw new BaseCustomException(SECRET_FILE_NOT_FOUND);
		} catch(IOException e) {
			throw new BaseCustomException(INVALID_SECRET_FILE);
		}

	}
}
