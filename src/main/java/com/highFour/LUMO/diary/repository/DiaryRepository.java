package com.highFour.LUMO.diary.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.highFour.LUMO.diary.entity.Diary;
import com.highFour.LUMO.diary.entity.DiaryType;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {
	// 오늘의 일기 존재여부 확인
	boolean existsByTypeAndCreatedAtBetween(DiaryType type, LocalDateTime start, LocalDateTime end);

	// 타입별 리스트 검색
	List<Diary> findByType(DiaryType type);

	// 타입별 제목에서 검색
	List<Diary> findByTypeAndTitleContainingIgnoreCase(DiaryType type, String title);

	// 타입별 본문에서 검색
	List<Diary> findByTypeAndContentsContainingIgnoreCase(DiaryType type, String title);

	// 삭제 후 30일지나면 영구 삭제
	List<Diary> findByDeletedAtBefore(LocalDateTime threshold);
}
