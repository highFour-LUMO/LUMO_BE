package com.highFour.LUMO.diary.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.highFour.LUMO.diary.dto.DiaryCreateReqDto;
import com.highFour.LUMO.diary.entity.Diary;
import com.highFour.LUMO.diary.service.DiaryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
public class DiaryController {

	private final DiaryService diaryService;

	@PostMapping("/create")
	public ResponseEntity<?> createReview(@RequestBody DiaryCreateReqDto reqDto) {
		Diary diary = diaryService.createDiary(reqDto);
		return new ResponseEntity<>(diary, HttpStatus.CREATED);
	}
}
