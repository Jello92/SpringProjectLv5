package com.example.board_spring5.like.service;

import com.example.board_spring5.board.entity.Board;
import com.example.board_spring5.board.repository.BoardRepository;
import com.example.board_spring5.comment.entity.Comment;
import com.example.board_spring5.comment.repository.CommentRepository;
import com.example.board_spring5.global.dto.StatusResponseDto;
import com.example.board_spring5.global.exception.ErrorException;
import com.example.board_spring5.global.exception.ExceptionEnum;
import com.example.board_spring5.like.entity.Like;
import com.example.board_spring5.like.repository.LikeRepository;
import com.example.board_spring5.user.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    public ResponseEntity updateBoardLike(Long id, Users users){
        Board board = checkBoard(id);
        boardLike(users, board);
        StatusResponseDto statusResponseDto;

        if(boardLike(users,board)){
            board.undoLike();
            likeRepository.deleteByUserIdAndBoardId(users.getId(),board.getId());
            statusResponseDto = new StatusResponseDto("좋아요를 취소하셨습니다.", HttpStatus.OK);
        } else {
            board.btnLike();
            Like like = new Like(users, board);
            likeRepository.save(like);
            statusResponseDto = new StatusResponseDto("좋아요를 누르셨습니다.",HttpStatus.OK);
        }
        return new ResponseEntity(statusResponseDto, HttpStatus.OK);
    }

    private Board checkBoard(Long id){
        return boardRepository.findById(id).orElseThrow(
                () -> new ErrorException(ExceptionEnum.BOARD_NOT_FOUND)
        );
    }

    private Comment checkComment(Long id){
        return commentRepository.findById(id).orElseThrow(
                () -> new ErrorException(ExceptionEnum.COMMENT_NOT_FOUND)
        );
    }

    private boolean boardLike(Users users, Board board){
        Optional<Like> like = likeRepository.findByUserIdAndBoardId(users.getId(), board.getId());
        if (like.isPresent()){
            return true;
        }
        return false;
    }

    private boolean commentLike(Users users, Board board, Comment comment){
        Optional<Like> like = likeRepository.findByUserIdAndBoardIdAndCommentId(users.getId(), board.getId(),comment.getId());
        if (like.isPresent()){
            return true;
        }
        return false;
    }
}
