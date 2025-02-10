package com.highFour.LUMO.diary.service;

import static com.highFour.LUMO.common.exceptionType.DiaryExceptionType.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.highFour.LUMO.common.exception.BaseCustomException;
import com.highFour.LUMO.diary.dto.DiaryCreateReqDto;
import com.highFour.LUMO.diary.entity.Category;
import com.highFour.LUMO.diary.entity.Diary;
import com.highFour.LUMO.diary.entity.DiaryImg;
import com.highFour.LUMO.diary.entity.DiaryType;
import com.highFour.LUMO.diary.entity.Emotion;
import com.highFour.LUMO.diary.repository.CategoryRepository;
import com.highFour.LUMO.diary.repository.DiaryImgRepository;
import com.highFour.LUMO.diary.repository.DiaryRepository;
import com.highFour.LUMO.diary.repository.EmotionRepository;

@Service
public class DiaryService {
	private final EmotionRepository emotionRepository;
	private final CategoryRepository categoryRepository;
	private final DiaryImgRepository diaryImgRepository;
	private final DiaryRepository diaryRepository;

	public DiaryService(EmotionRepository emotionRepository, CategoryRepository categoryRepository,
		DiaryImgRepository diaryImgRepository, DiaryRepository diaryRepository) {
		this.emotionRepository = emotionRepository;
		this.categoryRepository = categoryRepository;
		this.diaryImgRepository = diaryImgRepository;
		this.diaryRepository = diaryRepository;
	}

	@Transactional
	public Diary createDiary(DiaryCreateReqDto reqDto) {
		// 입력 null에 대한 예외 처리 필요

		Emotion emotion = new Emotion();
		Category category = new Category();

		if (reqDto.type() == DiaryType.DIARY) {
			emotion = emotionRepository.findById(reqDto.emotionId())
				.orElseThrow(() -> new BaseCustomException(EMOTION_NOT_FOUND));
			category = categoryRepository.findById(1L)
				.orElseThrow(() -> new BaseCustomException(CATEGORY_NOT_FOUND));
		} else if (reqDto.type() == DiaryType.GRATITUDE) {
			emotion = emotionRepository.findById(1L)
				.orElseThrow(() -> new BaseCustomException(EMOTION_NOT_FOUND));
			category = categoryRepository.findById(reqDto.categoryId())
				.orElseThrow(() -> new BaseCustomException(CATEGORY_NOT_FOUND));
		}

		Diary diary = reqDto.toEntity(emotion, category);
		diaryRepository.save(diary);

		List<DiaryImg> diaryImgs = reqDto.toDiaryImg(diary);
		diaryImgRepository.saveAll(diaryImgs);

		return diary;
	}
}