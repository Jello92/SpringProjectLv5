package com.example.board_spring5.like.service;

import com.example.board_spring5.board.entity.Board;
import com.example.board_spring5.board.repository.BoardRepository;
import com.example.board_spring5.comment.entity.Comment;
import com.example.board_spring5.comment.repository.CommentRepository;
import com.example.board_spring5.global.dto.StatusResponseDto;
import com.example.board_spring5.global.exception.ErrorException;
import com.example.board_spring5.global.exception.ExceptionEnum;
import com.example.board_spring5.like.entity.BoardLikes;
import com.example.board_spring5.like.entity.CommentLikes;
import com.example.board_spring5.like.repository.BoardLikeRepository;
import com.example.board_spring5.like.repository.CommentLikeRepository;
import com.example.board_spring5.user.entity.Users;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final CommentLikeRepository commentLikeRepository;

    public ResponseEntity updateBoardLike(Long boardId, Users users){
        Board board = checkBoard(boardId);
        boardLike(users, board);
        StatusResponseDto statusResponseDto;

        if(boardLike(users,board)){
            board.undoLike();
            boardLikeRepository.deleteByUsersIdAndBoardId(users.getId(),board.getId());
            statusResponseDto = new StatusResponseDto("좋아요를 취소하셨습니다.", HttpStatus.OK);
        } else {
            board.btnLike();
            BoardLikes boardLikes = new BoardLikes(users, board);
            boardLikeRepository.save(boardLikes);
            statusResponseDto = new StatusResponseDto("좋아요를 누르셨습니다.",HttpStatus.OK);
        }
        return new ResponseEntity(statusResponseDto, HttpStatus.OK);
    }

    public ResponseEntity updateCommentLike(Long boardId, Long commentId, Users users){
        Board board = checkBoard(boardId);
        Comment comment = checkComment(commentId);
        commentLike(users,board,comment);
        StatusResponseDto statusResponseDto;

        if(commentLike(users, board, comment)){
            comment.undoLike();
            commentLikeRepository.findByUsersIdAndBoardIdAndCommentId(users.getId(),boardId,commentId);
            statusResponseDto = new StatusResponseDto("좋아요를 취소하셨습니다.", HttpStatus.OK);
        } else {
            comment.btnLike();
            CommentLikes commentLikes = new CommentLikes(users, board, comment);
            commentLikeRepository.save(commentLikes);
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
        Optional<BoardLikes> like = boardLikeRepository.findByUsersIdAndBoardId(users.getId(), board.getId());
        if (like.isPresent()){
            return true;
        }
        return false;
    }

    private boolean commentLike(Users users, Board board, Comment comment){
        Optional<CommentLikes> like = commentLikeRepository.findByUsersIdAndBoardIdAndCommentId(users.getId(), board.getId(),comment.getId());
        if (like.isPresent()){
            return true;
        }
        return false;
    }
}
