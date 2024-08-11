package com.example.startingclubbackend.service.forumComment;

import com.example.startingclubbackend.DTO.comment.CommentDTO;
import com.example.startingclubbackend.model.forum.ForumComment;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;

public interface ForumCommentService {
    ResponseEntity<Object> postComment(@NotNull CommentDTO commentDTO);

    ResponseEntity<Object> likeComment(Long commentId);

    ResponseEntity<Object> fetchAllComments();

    ResponseEntity<Object> fetchCommentById(Long commentId);

    ResponseEntity<Object> postReply(@NotNull CommentDTO commentDTO, Long parentCommentId);

    ResponseEntity<Object> fetchAllRepliesByCommendId(Long parentCommentId);

    ResponseEntity<Object> editComment(@NotNull CommentDTO commentDTO, Long commentId);

    ResponseEntity<Object> deleteComment(Long commentId);

    void saveComment(@NotNull ForumComment comment);

    void deleteComment(@NotNull ForumComment comment);

    ForumComment getCommentById(Long commentId);
}
