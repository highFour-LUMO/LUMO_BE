package com.highFour.LUMO.diary.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.highFour.LUMO.diary.entity.Category;
import com.highFour.LUMO.diary.entity.Diary;
import com.highFour.LUMO.diary.entity.DiaryHashtagRelation;
import com.highFour.LUMO.diary.entity.DiaryImg;
import com.highFour.LUMO.diary.entity.DiaryType;
import com.highFour.LUMO.diary.entity.Emotion;
import com.highFour.LUMO.diary.entity.Hashtag;
import com.highFour.LUMO.diary.entity.Visibility;

import lombok.Builder;

@Builder
public record DiaryCreateReq(
	String title,
	String contents,
	List<String> hashtags,
	List<String> imgUrls,
	Long emotionId,
	Long categoryId,
	Long rating,
	DiaryType type,
	Visibility visibility
) {
	public Diary toEntity(Emotion emotion, Category category, Long memberId){
		return Diary.builder()
			.memberId(memberId)
			.type(this.type)
			.title(this.title)
			.contents(this.contents)
			.visibility(this.visibility)
			.emotion(emotion)
			.category(category)
			.rating(this.rating)
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

	public List<DiaryHashtagRelation> toDiaryHashtagRelation(Diary diary, List<Hashtag> hashtags) {
		return hashtags.stream()
			.map(tag -> DiaryHashtagRelation.builder()
				.diary(diary)
				.hashtag(tag)
				.build())
			.collect(Collectors.toList());
	}
}
