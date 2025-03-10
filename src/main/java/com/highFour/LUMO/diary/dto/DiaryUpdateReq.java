package com.highFour.LUMO.diary.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.highFour.LUMO.diary.entity.Visibility;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DiaryUpdateReq(
	Long diaryId,
	String title,
	String contents,
	List<String> hashtags,
	List<String> imgUrls,
	Long emotionId,
	Long categoryId,
	Long rating,
	Visibility visibility
){}
