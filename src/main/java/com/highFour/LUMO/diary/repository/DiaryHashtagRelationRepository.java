package com.highFour.LUMO.diary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.highFour.LUMO.diary.entity.DiaryHashtagRelation;

@Repository
public interface DiaryHashtagRelationRepository extends JpaRepository<DiaryHashtagRelation, Long> {
}
