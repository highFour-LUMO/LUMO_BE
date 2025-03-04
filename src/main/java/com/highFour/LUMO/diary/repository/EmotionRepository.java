package com.highFour.LUMO.diary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.highFour.LUMO.diary.entity.Emotion;

@Repository
public interface EmotionRepository extends JpaRepository<Emotion, Long> {
}
