package com.highFour.LUMO.diary.dto;

import java.util.List;

import com.highFour.LUMO.diary.entity.Category;
import com.highFour.LUMO.diary.entity.Diary;
import com.highFour.LUMO.diary.entity.DiaryImg;
import com.highFour.LUMO.diary.entity.Emotion;
import com.highFour.LUMO.diary.entity.Visibility;

import lombok.Builder;

@Builder
public record DiaryListResDto(
	String title,
	String contents,
	List<String> imgUrls,
	String member,
	String emotion,
	String category,
	Visibility visibility
) {

	public static DiaryListResDto fromEntity(Diary diary,  List<DiaryImg> diaryImgs) {
		List<String> imgUrls = diaryImgs.stream().map(DiaryImg::getImgUrl).toList();

		return DiaryListResDto.builder()
			.title(diary.getTitle())
			.contents(diary.getContents())
			.imgUrls(imgUrls)
			.member(null)
			.emotion(diary.getEmotion().getLabel())
			.category(diary.getCategory().getLabel())
			.visibility(diary.getVisibility())
			.build();
	}


}
