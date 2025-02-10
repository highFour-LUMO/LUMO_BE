package com.highFour.LUMO.member.repository;

import com.highFour.Hand2Hand.domain.member.entity.Member;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByEmail(@NotNull String email);

}
