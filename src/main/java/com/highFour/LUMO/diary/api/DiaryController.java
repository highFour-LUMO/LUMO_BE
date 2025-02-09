package com.highFour.LUMO.diary.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.highFour.LUMO.diary.dto.DiaryCreateReqDto;
import com.highFour.LUMO.diary.dto.DiaryListResDto;
import com.highFour.LUMO.diary.dto.DiarySearchReqDto;
import com.highFour.LUMO.diary.entity.Diary;
import com.highFour.LUMO.diary.service.DiaryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
public class DiaryController {

	private final DiaryService diaryService;

	// 일기 작성
	@PostMapping("/create")
	public ResponseEntity<?> createReview(@RequestBody DiaryCreateReqDto reqDto) {
		Diary diary = diaryService.createDiary(reqDto);
		return new ResponseEntity<>(diary, HttpStatus.CREATED);
	}

	// 일기 상세 조회
	@GetMapping("/{diaryId}")
	public ResponseEntity<?> getReviewList(@PathVariable(name = "diaryId") Long diaryId) {
		DiaryListResDto diaryList = diaryService.getDiaryByDiaryId(diaryId);
		return new ResponseEntity<>(diaryList, HttpStatus.CREATED);
	}

	// 	일기 목록 조회

	// 검색어로 검색
	@GetMapping("/search")
	public ResponseEntity<?>  searchByKeyword(@RequestBody DiarySearchReqDto dto) {
		List<DiaryListResDto> diaryList = diaryService.searchByKeyword(dto);
		return new ResponseEntity<>(diaryList, HttpStatus.CREATED);
	}

}
