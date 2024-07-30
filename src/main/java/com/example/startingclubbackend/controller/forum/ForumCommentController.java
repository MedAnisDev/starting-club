package com.example.startingclubbackend.controller.forum;

import com.example.startingclubbackend.DTO.comment.CommentDTO;
import com.example.startingclubbackend.service.forumComment.ForumCommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/comments")
public class ForumCommentController {
    private final ForumCommentService forumCommentService ;

    public ForumCommentController(ForumCommentService forumCommentService) {
        this.forumCommentService = forumCommentService;
    }

    @PostMapping("/post")
    public ResponseEntity<Object> postComment(@RequestBody CommentDTO commentDTO){
        return forumCommentService.postComment(commentDTO) ;
    }


}
