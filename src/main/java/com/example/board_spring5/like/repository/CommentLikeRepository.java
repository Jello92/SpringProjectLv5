package com.example.board_spring5.like.repository;

import com.example.board_spring5.comment.entity.Comment;
import com.example.board_spring5.like.entity.BoardLikes;
import com.example.board_spring5.like.entity.CommentLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLikes, Long> {
    Optional<CommentLikes> findByUsersIdAndBoardIdAndCommentId(Long USER_ID, Long BOARD_ID,Long COMMENT_ID);
}
