package com.highFour.LUMO.diary.dto;

import com.highFour.LUMO.diary.entity.Diary;

import lombok.Builder;

@Builder
public record DiaryListRes(
	String title,
	String contents,
	String member,
	String emotion,
	String category
) {

	public static DiaryListRes fromEntity(Diary diary) {
		return DiaryListRes.builder()
			.title(diary.getTitle())
			.contents(diary.getContents())
			.member(null)
			.emotion(diary.getEmotion().getLabel())
			.category(diary.getCategory().getLabel())
			.build();
	}
}
