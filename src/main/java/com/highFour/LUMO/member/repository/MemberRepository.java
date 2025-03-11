package com.highFour.LUMO.member.repository;

import com.highFour.LUMO.member.entity.Member;
import com.highFour.LUMO.member.entity.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findBySocialIdAndSocialType(String socialId, SocialType socialType);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    Optional<Member> findByRefreshToken(String refreshToken);

}
