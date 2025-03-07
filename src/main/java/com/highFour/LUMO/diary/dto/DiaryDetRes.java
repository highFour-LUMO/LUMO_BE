package com.highFour.LUMO.diary.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.highFour.LUMO.diary.entity.Diary;
import com.highFour.LUMO.diary.entity.DiaryImg;
import com.highFour.LUMO.diary.entity.Visibility;

import lombok.Builder;

@Builder
public record DiaryDetRes(
	String title,
	String contents,
	String member,
	String emotion,
	String category,
	Visibility visibility,
	List<String> imgUrls
){
	public static DiaryDetRes fromEntity(Diary diary, List<String> imgUrls) {
		return DiaryDetRes.builder()
			.title(diary.getTitle())
			.contents(diary.getContents())
			.member(null)
			.emotion(diary.getEmotion().getLabel())
			.category(diary.getCategory().getLabel())
			.imgUrls(imgUrls)
			.build();
	}

	public static List<String> fromDiaryImg(List<DiaryImg> diaryImgs) {
		return diaryImgs.stream()
			.map(DiaryImg::getImgUrl)
			.collect(Collectors.toList());
	}
}
