package com.highFour.LUMO.comment.dto;

import lombok.Builder;

@Builder
public record CommentReq(Long memberId,
                         Long diaryId,
                         Long parentCommentId,
                         String content){
}
