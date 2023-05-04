package com.example.board_spring5.like.repository;

import com.example.board_spring5.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like,Long> {
    Optional<Like> findByUserIdAndBoardId(Long memeber_id, Long board_id);

    Optional<Like> findByUserIdAndBoardIdAndCommentId(Long memeber_id, Long board_id, Long comment_id);

    void deleteByUserIdAndBoardId(Long memeber_id, Long board_id);

    void deleteByUserIdAndBoardIdAndCommentId(Long memeber_id, Long board_id, Long comment_id);
}
