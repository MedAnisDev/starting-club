package com.example.startingclubbackend.DTO.comment;

import com.example.startingclubbackend.DTO.user.UserPublicDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentDTO{
    @NotBlank(message = "please write something")
    private String content;

    private LocalDateTime createdAt;

    private int likesCount;

    private UserPublicDTO postedBy ;

    private CommentDTO parentCommentId; ;



}
