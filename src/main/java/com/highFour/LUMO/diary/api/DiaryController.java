package com.highFour.LUMO.diary.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
import com.highFour.LUMO.diary.dto.DiaryUpdateReq;
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
	@PostMapping("")
	public ResponseEntity<?> createDiary(@RequestBody DiaryCreateReq reqDto) {
		Diary diary = diaryService.createDiary(reqDto);
		return new ResponseEntity<>("일기/감사 일기 작성이 정상적으로 완료되었습니다.", HttpStatus.OK);
	}

	// 일기 상세 조회
	@GetMapping("/{diaryId}")
	public ResponseEntity<?> getDiaryDetail(@PathVariable(name = "diaryId") Long diaryId) {
		DiaryDetRes diary = diaryService.getDiaryByDiaryId(diaryId);
		return new ResponseEntity<>(diary, HttpStatus.OK);
	}

	// 	일기 목록 조회
	@GetMapping("")
	public ResponseEntity<?> getDiaryListByType(@RequestParam(required=false) DiaryType type) {
		List<DiaryListRes> diaryList = diaryService.getDiaryList(type);
		return new ResponseEntity<>(diaryList, HttpStatus.OK);
	}

	// 일기 삭제
	@DeleteMapping("/{diaryId}")
	public ResponseEntity<?>  deleteDiary(@PathVariable(name = "diaryId") Long diaryId) {
		 diaryService.deleteDiary(diaryId);
		return new ResponseEntity<>("일기/감사 일기가 임시보관함으로 이동되었습니다.", HttpStatus.OK);
	}

	// 일기 삭제
	@PatchMapping("")
	public ResponseEntity<?> updateDiary(@RequestBody DiaryUpdateReq reqDto) {
		diaryService.updateDiary(reqDto);
		return new ResponseEntity<>("일기/감사일기 수정이 완료되었습니다.", HttpStatus.OK);
	}

	// 검색어로 검색
	@GetMapping("/search")
	public ResponseEntity<?>  searchByKeyword(@RequestBody DiarySearchReq dto) {
		List<DiaryListRes> diaryList = diaryService.searchByKeyword(dto);
		return new ResponseEntity<>(diaryList, HttpStatus.OK);
	}

	// 주간 평균 점수
	@GetMapping("/rating/weekly")
	public ResponseEntity<?>  getWeeklyAvgRating() {
		Double weeklyAvgRating  = diaryService.getWeeklyAvgRating();
		return new ResponseEntity<>(weeklyAvgRating, HttpStatus.OK);
	}

	// 월간 평균 점수
	@GetMapping("/rating/monthly")
	public ResponseEntity<?>  getMonthlyAvgRating() {
		Double weeklyAvgRating  = diaryService.getMonthlyAvgRating();
		return new ResponseEntity<>(weeklyAvgRating, HttpStatus.OK);
	}


}
