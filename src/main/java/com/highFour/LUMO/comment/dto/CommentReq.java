package com.highFour.LUMO.comment.dto;

import com.highFour.LUMO.comment.entity.Comment;
import com.highFour.LUMO.diary.entity.Diary;
import com.highFour.LUMO.member.entity.Member;
import lombok.Builder;

@Builder
public record CommentReq(Long diaryId,
                         Long parentCommentId,
                         String content) {

    public Comment toEntity(Member member, Diary diary, Comment parentComment) {
        return Comment.builder()
                .member(member)
                .diary(diary)
                .parentComment(parentComment)
                .content(content)
                .build();
    }

}
