package com.highFour.LUMO.comment.api;

import com.highFour.LUMO.comment.dto.CommentReq;
import com.highFour.LUMO.comment.dto.CommentRes;
import com.highFour.LUMO.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentRes> createComment(@RequestBody CommentReq requestDto) {
        CommentRes commentRes = commentService.createComment(requestDto);
        return new ResponseEntity<>(commentRes, HttpStatus.OK);
    }

}
