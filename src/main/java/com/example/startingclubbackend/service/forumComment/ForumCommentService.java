package com.example.startingclubbackend.service.forumComment;

import com.example.startingclubbackend.DTO.comment.CommentDTO;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;

public interface ForumCommentService {
    ResponseEntity<Object> postComment(@NotNull final CommentDTO commentDTO);
}
