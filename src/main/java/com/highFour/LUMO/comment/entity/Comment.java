package com.highFour.LUMO.comment.entity;

import com.highFour.LUMO.common.domain.BaseTimeEntity;
import com.highFour.LUMO.diary.entity.Diary;
import com.highFour.LUMO.member.entity.DelYn;
import com.highFour.LUMO.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id", nullable = false)
    private Diary diary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @Column(length = 150, nullable = false)
    private String content;

    public void updateContent(String newContent) {
        this.content = newContent;
    }

    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "del_yn", nullable = false)
    private DelYn delYn = DelYn.N;

    public void updateDeleted() {
        this.delYn = DelYn.Y;
    }

    public boolean isDeleted() {
        return this.delYn == DelYn.Y;
    }

}
