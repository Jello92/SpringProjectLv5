package com.example.board_spring5.like.controller;

import com.example.board_spring5.global.security.UserDetailsImpl;
import com.example.board_spring5.like.service.LikeService;
import com.example.board_spring5.user.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/like")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{boardId}")
    public ResponseEntity<?> updateBoardLike (@PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails){
    return likeService.updateBoardLike(boardId, userDetails.getUser());
    }

    @PostMapping("/{boardId}/{commentId}")
    public ResponseEntity<?> updateCommentLike (@PathVariable Long boardId, @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return likeService.updateCommentLike(boardId, commentId,userDetails.getUser());
    }
}
