package com.highFour.LUMO.diary.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.highFour.LUMO.diary.entity.Diary;
import com.highFour.LUMO.diary.entity.DiaryType;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {
	boolean existsByTypeAndCreatedAtBetween(DiaryType type, LocalDateTime start, LocalDateTime end);
}
