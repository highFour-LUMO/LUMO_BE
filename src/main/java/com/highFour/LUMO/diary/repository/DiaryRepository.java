package com.highFour.LUMO.diary.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.highFour.LUMO.diary.entity.Diary;
import com.highFour.LUMO.diary.entity.DiaryType;
import com.highFour.LUMO.member.entity.Member;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {
	// 오늘의 일기 존재여부 확인
	boolean existsByMemberIdAndTypeAndCreatedAtBetween(Long memberId, DiaryType type, LocalDateTime start, LocalDateTime end);

	// 타입별 리스트 검색
	Page<Diary> findByMemberAndType(Member member, DiaryType type, Pageable pageable);

	// 사용자별 전체 리스트 조회
	Page<Diary> findAllByMember(Member member, Pageable pageable);

	// 타입별 제목에서 검색
	Page<Diary> findByTypeAndTitleContainingIgnoreCase(DiaryType type, String title, Pageable pageable);

	// 타입별 본문에서 검색
	Page<Diary> findByTypeAndContentsContainingIgnoreCase(DiaryType type, String title, Pageable pageable);

	// 삭제 후 30일지나면 영구 삭제
	List<Diary> findByDeletedAtBefore(LocalDateTime threshold);

	//  점수 계산
	List<Diary> findByMemberIdAndTypeAndCreatedAtBetween(Long memberId, DiaryType type, LocalDateTime startDate, LocalDateTime endDate);

}
