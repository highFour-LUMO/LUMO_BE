package com.highFour.LUMO.diary.dto;

import com.highFour.LUMO.diary.entity.Diary;
import com.highFour.LUMO.diary.entity.Visibility;

import lombok.Builder;

@Builder
public record DiaryListRes(
	String title,
	String contents,
	String nickname,
	String emotion,
	String category,
	Visibility visibility

) {

	public static DiaryListRes fromEntity(Diary diary) {
		return DiaryListRes.builder()
			.title(diary.getTitle())
			.contents(diary.getContents())
			.nickname(diary.getMember().getNickname())
			.emotion(diary.getEmotion().getLabel())
			.category(diary.getCategory().getLabel())
			.visibility(diary.getVisibility())
			.build();
	}
}
