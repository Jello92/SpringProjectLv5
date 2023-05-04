package com.example.board_spring5.like.controller;

import com.example.board_spring5.global.security.UserDetailsImpl;
import com.example.board_spring5.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/like")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{id}")
    public ResponseEntity<?> updateBoardLike (@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
    return likeService.updateBoardLike(id, userDetails.getUser());

    }
}
