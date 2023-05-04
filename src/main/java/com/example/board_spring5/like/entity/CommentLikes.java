package com.example.board_spring5.like.entity;

import com.example.board_spring5.board.entity.Board;
import com.example.board_spring5.comment.entity.Comment;
import com.example.board_spring5.user.entity.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CommentLikes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LIKE_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private Users users;

    @ManyToOne
    @JoinColumn(name = "BOARD_ID")
    private Board board;

    @ManyToOne
    @JoinColumn (name = "COMMENT_ID")
    private Comment comment;

    public CommentLikes(Users users, Board board, Comment comment) {
        this.users = users;
        this.board = board;
        this.comment = comment;
    }
}