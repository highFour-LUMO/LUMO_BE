package com.highFour.LUMO.diary.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.highFour.LUMO.diary.dto.DiaryCreateReq;
import com.highFour.LUMO.diary.dto.DiaryDetRes;
import com.highFour.LUMO.diary.dto.DiaryListRes;
import com.highFour.LUMO.diary.dto.DiarySearchReq;
import com.highFour.LUMO.diary.entity.Diary;
import com.highFour.LUMO.diary.entity.DiaryType;
import com.highFour.LUMO.diary.service.DiaryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
public class DiaryController {

	private final DiaryService diaryService;

	// 일기 작성
	@PostMapping("/create")
	public ResponseEntity<?> createDiary(@RequestBody DiaryCreateReq reqDto) {
		Diary diary = diaryService.createDiary(reqDto);
		return new ResponseEntity<>(diary, HttpStatus.CREATED);
	}

	// 일기 상세 조회
	@GetMapping("/{diaryId}")
	public ResponseEntity<?> getDiaryDetail(@PathVariable(name = "diaryId") Long diaryId) {
		DiaryDetRes diary = diaryService.getDiaryByDiaryId(diaryId);
		return new ResponseEntity<>(diary, HttpStatus.CREATED);
	}

	// 	일기 목록 조회
	@GetMapping("/list")
	public ResponseEntity<?> getDiaryListByType(@RequestParam DiaryType type) {
		List<DiaryListRes> diaryList = diaryService.getDiaryList(type);
		return new ResponseEntity<>(diaryList, HttpStatus.CREATED);
	}

	// 검색어로 검색
	@GetMapping("/search")
	public ResponseEntity<?>  searchByKeyword(@RequestBody DiarySearchReq dto) {
		List<DiaryListRes> diaryList = diaryService.searchByKeyword(dto);
		return new ResponseEntity<>(diaryList, HttpStatus.CREATED);
	}

	@GetMapping("/delete/{diaryId}")
	public ResponseEntity<?>  deleteDiary(@PathVariable(name = "diaryId") Long diaryId) {
		 diaryService.deleteDiary(diaryId);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

}
