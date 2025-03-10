package com.highFour.LUMO.friend.entity;

import com.highFour.LUMO.common.domain.BaseTimeEntity;
import com.highFour.LUMO.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Friend extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member1_id", nullable = false)
    private Member member1; // 친구 관계의 첫 번째 회원

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member2_id", nullable = false)
    private Member member2; // 친구 관계의 두 번째 회원

    public void softDeleteFriend() {
        setDeletedAt(LocalDateTime.now());
    }
}
