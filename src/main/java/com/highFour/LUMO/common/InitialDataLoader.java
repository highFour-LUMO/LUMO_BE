package com.highFour.LUMO.common;

import org.springframework.boot.CommandLineRunner;
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

	@Override
	public void run(String... args) throws Exception {
		insertCategory();
		insertEmotions();
	}


	private void insertCategory() {
		Category category = new Category(null, "없음");	// 일기의 경우
		Category category1 = new Category(null, "가족");
		Category category2 = new Category(null, "건강");
		Category category3 = new Category(null, "일");
		Category category4 = new Category(null, "기타");

		categoryRepository.save(category);
		categoryRepository.save(category1);
		categoryRepository.save(category2);
		categoryRepository.save(category3);
		categoryRepository.save(category4);
	}

	private void insertEmotions() {
		String[] labels = {"없음", "기쁨", "슬픔", "분노", "평온", "행복", "우울", "설렘", "신남", "긴장", "안도"};
		// 감사 일기의 경우 감정은 없음

		for (int i = 0; i < labels.length; i++) {
			emotionRepository.save(new Emotion(null, labels[i], null));
		}
	}


}
