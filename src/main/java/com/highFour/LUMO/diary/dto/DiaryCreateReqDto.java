package com.highFour.LUMO.diary.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.highFour.LUMO.diary.entity.Category;
import com.highFour.LUMO.diary.entity.Diary;
import com.highFour.LUMO.diary.entity.DiaryImg;
import com.highFour.LUMO.diary.entity.DiaryType;
import com.highFour.LUMO.diary.entity.Emotion;
import com.highFour.LUMO.diary.entity.Visibility;

import lombok.Builder;

@Builder
public record DiaryCreateReqDto(
	Long memberId,
	String title,
	String contents,
	List<String> imgUrls,
	Long emotionId,
	Long categoryId,
	DiaryType type,
	Visibility visibility
) {
	public Diary toEntity(Emotion emotion, Category category){
		return Diary.builder()
			.memberId(this.memberId)
			.type(this.type)
			.title(this.title)
			.contents(this.contents)
			.visibility(this.visibility)
			.emotion(emotion)
			.category(category)
			.build();
	}

	public List<DiaryImg> toDiaryImg(Diary diary) {
		return imgUrls.stream()
			.map(imgUrl -> DiaryImg.builder()
				.diary(diary)
				.imgUrl(imgUrl)
				.build())
			.collect(Collectors.toList());
	}
}
