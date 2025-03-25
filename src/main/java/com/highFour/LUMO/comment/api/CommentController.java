package com.highFour.LUMO.comment.api;

import com.highFour.LUMO.comment.dto.CommentReq;
import com.highFour.LUMO.comment.dto.CommentRes;
import com.highFour.LUMO.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody CommentReq commentReq) {
        CommentRes commentRes = commentService.createComment(commentReq);
        return new ResponseEntity<>(commentRes, HttpStatus.OK);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long commentId, @RequestBody Map<String, String> requestBody) {
        String newContent = requestBody.get("content");
        CommentRes updatedComment = commentService.updateComment(commentId, newContent);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return new ResponseEntity<>("댓글이 삭제되었습니다.", HttpStatus.OK);
    }

}
