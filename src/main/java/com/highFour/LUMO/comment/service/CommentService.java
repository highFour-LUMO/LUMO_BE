package com.highFour.LUMO.comment.service;

import com.highFour.LUMO.comment.dto.CommentReq;
import com.highFour.LUMO.comment.dto.CommentRes;
import com.highFour.LUMO.comment.entity.Comment;
import com.highFour.LUMO.comment.repository.CommentRepository;
import com.highFour.LUMO.common.exception.BaseCustomException;
import com.highFour.LUMO.common.exceptionType.CommentExceptionType;
import com.highFour.LUMO.common.exceptionType.DiaryExceptionType;
import com.highFour.LUMO.common.exceptionType.MemberExceptionType;
import com.highFour.LUMO.diary.entity.Diary;
import com.highFour.LUMO.diary.repository.DiaryRepository;
import com.highFour.LUMO.member.entity.Member;
import com.highFour.LUMO.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public CommentRes createComment(CommentReq commentReq) {
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Long memberId = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new BaseCustomException(MemberExceptionType.MEMBER_NOT_FOUND)).getId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException(MemberExceptionType.MEMBER_NOT_FOUND.message()));

        Diary diary = diaryRepository.findById(commentReq.diaryId())
                .orElseThrow(() -> new EntityNotFoundException(DiaryExceptionType.DIARY_NOT_FOUND.message()));

        Comment parentComment = null;
        if (commentReq.parentCommentId() != null) {
            parentComment = commentRepository.findById(commentReq.parentCommentId())
                    .orElseThrow(() -> new EntityNotFoundException(CommentExceptionType.COMMENT_NOT_FOUND.message()));
        }

        Comment comment = commentReq.toEntity(member, diary, parentComment);
        commentRepository.save(comment);

        return CommentRes.fromEntity(comment);
    }

    @Transactional
    public CommentRes updateComment(Long commentId, String newContent) {
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new BaseCustomException(MemberExceptionType.MEMBER_NOT_FOUND));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException(CommentExceptionType.COMMENT_NOT_FOUND.message()));

        if (!comment.getMember().getId().equals(member.getId())) {
            throw new BaseCustomException(CommentExceptionType.UNAUTHORIZED_COMMENT_EDIT);
        }

        comment.updateContent(newContent);

        return CommentRes.fromEntity(comment);
    }

}
