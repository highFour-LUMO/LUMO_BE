package com.highFour.LUMO.comment.service;

import com.highFour.LUMO.comment.dto.CommentReq;
import com.highFour.LUMO.comment.dto.CommentRes;
import com.highFour.LUMO.comment.entity.Comment;
import com.highFour.LUMO.comment.repository.CommentRepository;
import com.highFour.LUMO.common.exceptionType.CommentExceptionType;
import com.highFour.LUMO.common.exceptionType.DiaryExceptionType;
import com.highFour.LUMO.common.exceptionType.MemberExceptionType;
import com.highFour.LUMO.diary.entity.Diary;
import com.highFour.LUMO.diary.repository.DiaryRepository;
import com.highFour.LUMO.member.entity.Member;
import com.highFour.LUMO.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final DiaryRepository diaryRepository;

    @Transactional
    public CommentRes createComment(CommentReq requestDto) {
        Member member = memberRepository.findById(requestDto.memberId())
                .orElseThrow(() -> new EntityNotFoundException(MemberExceptionType.MEMBER_NOT_FOUND.message()));

        Diary diary = diaryRepository.findById(requestDto.diaryId())
                .orElseThrow(() -> new EntityNotFoundException(DiaryExceptionType.DIARY_NOT_FOUND.message()));

        Comment parentComment = null;
        if (requestDto.parentCommentId() != null) {
            parentComment = commentRepository.findById(requestDto.parentCommentId())
                    .orElseThrow(() -> new EntityNotFoundException(CommentExceptionType.COMMENT_NOT_FOUND.message()));
        }

        // 댓글 저장
        Comment comment = Comment.builder()
                .member(member)
                .diary(diary)
                .parentComment(parentComment)
                .content(requestDto.content())
                .build();
        commentRepository.save(comment);

        return CommentRes.fromEntity(comment);
    }
}
