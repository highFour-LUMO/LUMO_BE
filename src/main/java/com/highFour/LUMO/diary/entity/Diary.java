package com.highFour.LUMO.diary.entity;

import com.highFour.LUMO.common.domain.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import net.minidev.json.annotate.JsonIgnore;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Diary extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emotion_id")
	@JsonIgnore
	private Emotion emotion;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	@JsonIgnore
	private Category category;

	private Long memberId;

	@Enumerated(EnumType.STRING)
	private DiaryType type;

	@Column(length = 50)
	private String title;

	@Column(length = 3000)
	private String contents;

	private Long rating;

	@Enumerated(EnumType.STRING)
	private Visibility visibility;

	public void softDeleteDiary() {
		setDeletedAt(LocalDateTime.now());
	}

	public void updateTitle(String title) {
		this.title = title;
	}

	public void updateContents(String contents) {
		this.contents = contents;
	}

	public void updateRating(Long rating) {
		this.rating = rating;
	}

	public void updateEmotion(Emotion emotion) {
		this.emotion = emotion;
	}

	public void updateCategory(Category category) {
		this.category = category;
	}

	public void updateVisibility(Visibility visibility) {
		this.visibility = visibility;
	}
}
