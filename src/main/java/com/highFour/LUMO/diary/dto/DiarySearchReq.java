package com.highFour.LUMO.diary.dto;

import com.highFour.LUMO.diary.entity.DiaryType;

import lombok.Builder;

@Builder
public record DiarySearchReq(
	DiaryType type,
	String searchType,
	String keyword
) {

}
