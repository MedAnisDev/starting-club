package com.example.startingclubbackend.service.forumComment;

import com.example.startingclubbackend.DTO.comment.CommentDTO;
import com.example.startingclubbackend.DTO.comment.CommentDTOMapper;
import com.example.startingclubbackend.model.forum.ForumComment;
import com.example.startingclubbackend.model.user.User;
import com.example.startingclubbackend.repository.ForumCommentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ForumCommentServiceImpl implements ForumCommentService{
    private final ForumCommentRepository forumCommentRepository ;
    private final CommentDTOMapper commentDTOMapper;

    public ForumCommentServiceImpl(ForumCommentRepository forumCommentRepository, CommentDTOMapper commentDTOMapper) {
        this.forumCommentRepository = forumCommentRepository;
        this.commentDTOMapper = commentDTOMapper;
    }

    public ResponseEntity<Object> postComment(final CommentDTO commentDTO) {
        ForumComment comment = setCommentFields(commentDTO) ;
        forumCommentRepository.save(comment) ;

        final CommentDTO commentResponse = commentDTOMapper.apply(comment) ;
        return new ResponseEntity<>(commentResponse , HttpStatus.CREATED) ;
    }

    @Override
    public ResponseEntity<Object> likeComment(final Long commentId) {
        final ForumComment comment = getCommentById(commentId) ;
        comment.setLikesCount(comment.getLikesCount()+ 1);
        forumCommentRepository.save(comment) ;

        return ResponseEntity.ok().body("Comment liked successfully") ;
    }

    @Override
    public ResponseEntity<Object> fetchAllComments() {
        final List <ForumComment> comments =forumCommentRepository.findAll() ;
        final List<CommentDTO> commentDTOLlist = comments.stream()
                .map(commentDTOMapper)
                .toList();
        return new ResponseEntity<>(commentDTOLlist , HttpStatus.OK) ;
    }

    @Override
    public ResponseEntity<Object> fetchCommentById(Long commentId) {
        final ForumComment comment = getCommentById(commentId) ;
        final CommentDTO commentDTO = commentDTOMapper.apply(comment) ;
        return new ResponseEntity<>(commentDTO , HttpStatus.OK) ;
    }

    @Override
    public ResponseEntity<Object> postReply(final CommentDTO commentDTO ,final Long parentCommentId) {

        ForumComment reply = setCommentFields(commentDTO) ;
        ForumComment parentComment =getCommentById(parentCommentId) ;

        reply.setParentComment(parentComment);
        parentComment.getReplies().add(reply) ;
        forumCommentRepository.save(reply) ;

        final CommentDTO replyResponse = commentDTOMapper.apply(reply) ;
        return new ResponseEntity<>(replyResponse , HttpStatus.CREATED) ;

    }

    @Override
    public ResponseEntity<Object> fetchAllRepliesByCommendId(Long parentCommentId) {
        List<ForumComment> replies = forumCommentRepository.fetchAllRepliesByCommendId(parentCommentId) ;
        final List<CommentDTO> repliesDTOResponse = replies.
                stream().map(commentDTOMapper).
                toList() ;
        return new ResponseEntity<>(repliesDTOResponse , HttpStatus.OK) ;
    }

    private ForumComment setCommentFields(final CommentDTO commentDTO ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication() ;
        User user = (User) auth.getPrincipal();

        ForumComment comment = new ForumComment() ;
        comment.setContent(commentDTO.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setPostedBy(user);
        return comment ;
    }

    private ForumComment getCommentById(Long commentId) {
        return forumCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("comment not found")) ;
    }

}
