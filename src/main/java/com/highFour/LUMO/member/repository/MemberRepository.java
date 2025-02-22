package com.highFour.LUMO.member.repository;


import com.highFour.LUMO.common.exception.BaseCustomException;
import com.highFour.LUMO.member.entity.Member;
import com.highFour.LUMO.member.entity.SocialType;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

import static com.highFour.LUMO.common.exceptionType.MemberExceptionType.MEMBER_NOT_FOUND;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findBySocialIdAndSocialType(String socialId, SocialType socialType);

    default Member findByIdOrThrow(Long memberId) {
        return findById(memberId).orElseThrow(() -> new BaseCustomException(MEMBER_NOT_FOUND));
    }


    @Query("SELECT m FROM Member m WHERE m.id IN :ids")
    List<Member> findByAllByIds(@Param("ids") List<Long> ids);

    @Query(value = "SELECT m.* FROM member AS m WHERE m.id IN :ids", nativeQuery = true)
    List<Member> findAllByIdContainDeleted(List<Long> ids);

}
