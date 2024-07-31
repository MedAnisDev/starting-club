package com.example.startingclubbackend.controller.forum;

import com.example.startingclubbackend.DTO.comment.CommentDTO;
import com.example.startingclubbackend.service.forumComment.ForumCommentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/comments")
public class ForumCommentController {
    private final ForumCommentService forumCommentService ;

    public ForumCommentController(ForumCommentService forumCommentService) {
        this.forumCommentService = forumCommentService;
    }

    @PostMapping("/post_Comment")
    public ResponseEntity<Object> postComment(final @Valid @RequestBody CommentDTO commentDTO){
        return forumCommentService.postComment(commentDTO) ;
    }

    @PostMapping("/{parentCommentId}/post_reply")
    public ResponseEntity<Object> postReply(final @Valid @RequestBody CommentDTO commentDTO ,final @PathVariable Long parentCommentId ){
        return forumCommentService.postReply(commentDTO , parentCommentId) ;
    }
    @GetMapping()
    public ResponseEntity<Object> fetchAllComments(){
        return forumCommentService.fetchAllComments() ;
    }
    @GetMapping("/{commentId}")
    public ResponseEntity<Object> fetchCommentById(@PathVariable final Long commentId){
        return forumCommentService.fetchCommentById(commentId) ;
    }

    @GetMapping("/{parentCommentId}/replies")
    public ResponseEntity<Object> fetchAllRepliesByCommendId(@PathVariable final Long parentCommentId ){
        return forumCommentService.fetchAllRepliesByCommendId(parentCommentId) ;
    }

    @PutMapping("/like/{commentId}")
    public ResponseEntity<Object> likeComment(final @PathVariable Long commentId){
        return forumCommentService.likeComment(commentId) ;
    }

    @PutMapping("/editComment/{commentId}")
    public ResponseEntity<Object> editComment(final @Valid @RequestBody CommentDTO commentDTO ,final @PathVariable Long commentId){
        return forumCommentService.editComment(commentDTO , commentId) ;
    }

    @DeleteMapping("/deleteComment/{commentId}")
    public ResponseEntity<Object> deleteComment(final @PathVariable Long commentId){
        return forumCommentService.deleteComment( commentId) ;
    }



}
