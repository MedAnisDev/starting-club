package com.example.startingclubbackend.service.forumComment;

import com.example.startingclubbackend.DTO.comment.CommentDTO;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;

public interface ForumCommentService {
    ResponseEntity<Object> postComment(@NotNull final CommentDTO commentDTO);

    ResponseEntity<Object> likeComment(final Long commentId);

    ResponseEntity<Object> fetchAllComments();

    ResponseEntity<Object> fetchCommentById(final Long commentId);

    ResponseEntity<Object> postReply(@NotNull final CommentDTO commentDTO ,final Long parentCommentId);

    ResponseEntity<Object> fetchAllRepliesByCommendId(final Long parentCommentId);

    ResponseEntity<Object> editComment(@NotNull final CommentDTO commentDTO, final Long commentId);

    ResponseEntity<Object> deleteComment(final Long commentId);
}
