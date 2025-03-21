package com.highFour.LUMO.comment.dto;

import com.highFour.LUMO.comment.entity.Comment;
import lombok.Builder;

@Builder
public record CommentRes(Long id,
                              Long memberId,
                              Long diaryId,
                              Long parentCommentId,
                              String content) {
    public static CommentRes fromEntity(Comment comment) {
        return CommentRes.builder()
                .id(comment.getId())
                .memberId(comment.getMember().getId())
                .diaryId(comment.getDiary().getId())
                .parentCommentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null)
                .content(comment.getContent())
                .build();
    }
}
