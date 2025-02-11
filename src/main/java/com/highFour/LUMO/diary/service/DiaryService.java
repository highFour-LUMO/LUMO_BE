package com.highFour.LUMO.diary.service;

import static com.highFour.LUMO.common.exceptionType.DiaryExceptionType.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.highFour.LUMO.common.domain.BaseTimeEntity;
import com.highFour.LUMO.common.exception.BaseCustomException;
import com.highFour.LUMO.diary.dto.DiaryCreateReqDto;
import com.highFour.LUMO.diary.dto.DiaryDetResDto;
import com.highFour.LUMO.diary.dto.DiaryListResDto;
import com.highFour.LUMO.diary.dto.DiarySearchReqDto;
import com.highFour.LUMO.diary.entity.Category;
import com.highFour.LUMO.diary.entity.Diary;
import com.highFour.LUMO.diary.entity.DiaryHashtagRelation;
import com.highFour.LUMO.diary.entity.DiaryImg;
import com.highFour.LUMO.diary.entity.DiaryType;
import com.highFour.LUMO.diary.entity.Emotion;
import com.highFour.LUMO.diary.entity.Hashtag;
import com.highFour.LUMO.diary.repository.CategoryRepository;
import com.highFour.LUMO.diary.repository.DiaryHashtagRelationRepository;
import com.highFour.LUMO.diary.repository.DiaryImgRepository;
import com.highFour.LUMO.diary.repository.DiaryRepository;
import com.highFour.LUMO.diary.repository.EmotionRepository;
import com.highFour.LUMO.diary.repository.HashtagRepository;

@Service
public class DiaryService {
	private final EmotionRepository emotionRepository;
	private final CategoryRepository categoryRepository;
	private final HashtagRepository hashtagRepository;
	private final DiaryImgRepository diaryImgRepository;
	private final DiaryRepository diaryRepository;
	private final DiaryHashtagRelationRepository diaryHashtagRelationRepository;

	public DiaryService(EmotionRepository emotionRepository, CategoryRepository categoryRepository,
		HashtagRepository hashtagRepository,
		DiaryImgRepository diaryImgRepository, DiaryRepository diaryRepository,
		DiaryHashtagRelationRepository diaryHashtagRelationRepository) {
		this.emotionRepository = emotionRepository;
		this.categoryRepository = categoryRepository;
		this.hashtagRepository = hashtagRepository;
		this.diaryImgRepository = diaryImgRepository;
		this.diaryRepository = diaryRepository;
		this.diaryHashtagRelationRepository = diaryHashtagRelationRepository;
	}

	// 일기 작성
	@Transactional
	public Diary createDiary(DiaryCreateReqDto reqDto) {
		// 입력 null에 대한 예외 처리 필요

		// 오늘의 일기가 이미 존재하는지 확인
		LocalDate today = LocalDate.now();
		LocalDateTime startOfDay = today.atStartOfDay();
		LocalDateTime endOfDay = today.atTime(23, 59, 59);

		boolean diaryExists = diaryRepository.existsByTypeAndCreatedAtBetween(
			reqDto.type(), startOfDay, endOfDay
		);
		if (diaryExists) {
			throw new BaseCustomException(DIARY_ALREADY_EXIST);
		}

		// 일기 or 감사일기에 따른 감정, 카테고리 저장
		Emotion emotion = null;
		Category category = null;

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
		List<Hashtag> hashtags = toHashtags(reqDto.hashtags());
		List<DiaryHashtagRelation> diaryHashtagRelations = reqDto.toDiaryHashtagRelation(diary, hashtags);

		diaryRepository.save(diary);
		hashtagRepository.saveAll(hashtags);
		diaryHashtagRelationRepository.saveAll(diaryHashtagRelations);

		List<DiaryImg> diaryImgs = reqDto.toDiaryImg(diary);
		diaryImgRepository.saveAll(diaryImgs);

		return diary;
	}

	// 해시 태그 중복 확인
	public List<Hashtag> toHashtags(List<String> hashtags) {
		return hashtags.stream()
			.distinct()
			.map(name -> hashtagRepository.findByName(name)
				.orElseGet(() -> Hashtag.builder().name(name).build()))
			.collect(Collectors.toList());
	}

	// 일기 상세 조회
	public DiaryDetResDto getDiaryByDiaryId(Long diaryId) {
		Diary diary = diaryRepository.findById(diaryId)
			.orElseThrow(() -> new BaseCustomException(DIARY_NOT_FOUND));

		List<DiaryImg> urls = diaryImgRepository.findByDiaryId(diaryId);
		List<String> imgs = DiaryDetResDto.fromDiaryImg(urls);

		return DiaryDetResDto.fromEntity(diary, imgs);
	}

	// 일기 목록 조회

	// 제목에서 검색
	public List<DiaryListResDto> searchByKeyword(DiarySearchReqDto dto) {
		List<Diary> diaryList = null;

		if (dto.searchType().equals("제목")) {
			diaryList = diaryRepository.findByTypeAndTitleContainingIgnoreCase(dto.type(), dto.keyword());
		}else if (dto.searchType().equals("내용")) {
			diaryList = diaryRepository.findByTypeAndContentsContainingIgnoreCase(dto.type(), dto.keyword());
		}
		return diaryList.stream()
			.map(DiaryListResDto::fromEntity)
			.collect(Collectors.toList());
	}

	// 일기 삭제 시 임시보관함에 저장
	public void deleteDiary(Long diaryId) {
		Diary diary = diaryRepository.findById(diaryId)
			.orElseThrow(() -> new BaseCustomException(DIARY_NOT_FOUND));

		// 임시보관함에 저장
		diary.softDeleteDiary();
	}

	// 임시 보관 30일 이후 영구 삭제
	@Scheduled(cron = "0 0 0 * * ?")
	@Transactional
	public void deleteExpiredDiaries() {
		LocalDateTime threshold = LocalDateTime.now().minusDays(30);
		List<Diary> expiredDiaries = diaryRepository.findByDeletedAtBefore(threshold);
		diaryRepository.deleteAll(expiredDiaries);
	}


}