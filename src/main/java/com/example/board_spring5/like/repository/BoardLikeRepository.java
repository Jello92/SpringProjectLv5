package com.example.board_spring5.like.repository;

import com.example.board_spring5.like.entity.BoardLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardLikeRepository extends JpaRepository<BoardLikes,Long> {
    Optional<BoardLikes> findByUsersIdAndBoardId(Long USER_ID, Long BOARD_ID);
    void deleteByUsersIdAndBoardId(Long USER_ID, Long BOARD_ID);
}
