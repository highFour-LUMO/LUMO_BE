package com.highFour.LUMO.diary.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.highFour.LUMO.diary.entity.DiaryImg;

@Repository
public interface DiaryImgRepository extends JpaRepository<DiaryImg, Long> {
	List<DiaryImg> findByDiaryId(Long diaryId);
}
