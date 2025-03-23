package com.highFour.LUMO.common;

import com.highFour.LUMO.member.entity.DelYn;
import com.highFour.LUMO.member.entity.Member;
import com.highFour.LUMO.member.entity.Role;
import com.highFour.LUMO.member.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.highFour.LUMO.diary.entity.Category;
import com.highFour.LUMO.diary.entity.Emotion;
import com.highFour.LUMO.diary.repository.CategoryRepository;
import com.highFour.LUMO.diary.repository.EmotionRepository;


import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InitialDataLoader implements CommandLineRunner {

	private final CategoryRepository categoryRepository;
	private final EmotionRepository emotionRepository;
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
		insertCategory();
		insertEmotions();
		insertMembers();
	}

	private void insertCategory() {
		Category[] categories = {
				new Category(null, "없음"),
				new Category(null, "가족"),
				new Category(null, "건강"),
				new Category(null, "일"),
				new Category(null, "기타")
		};

		for (Category category : categories) {
			categoryRepository.save(category);
		}
	}

	private void insertEmotions() {
		String[] labels = {"없음", "기쁨", "슬픔", "분노", "평온", "행복", "우울", "설렘", "신남", "긴장", "안도"};

		for (String label : labels) {
			emotionRepository.save(new Emotion(null, label, null));
		}
	}

	private void insertMembers() {
		Member[] members = {
				Member.builder()
						.email("test@test.com")
						.password(passwordEncoder.encode("testtest"))
						.nickname("test")
						.socialType(null)  // 소셜 타입 없음
						.socialId(null)
						.profileUrl("")
						.point(0) // 기본 포인트 값 설정
						.role(Role.MEMBER) // 기본 역할 설정
						.refreshToken(null)
						.delYn(DelYn.N)
						.build()
		};

		for (Member member : members) {
			if (memberRepository.findByEmail(member.getEmail()).isEmpty()) {
				memberRepository.save(member);
			}
		}
	}


}
