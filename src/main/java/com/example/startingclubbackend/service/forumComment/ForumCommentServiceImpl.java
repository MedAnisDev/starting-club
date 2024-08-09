package com.example.startingclubbackend.service.forumComment;

import com.example.startingclubbackend.DTO.comment.CommentDTO;
import com.example.startingclubbackend.DTO.comment.CommentDTOMapper;
import com.example.startingclubbackend.exceptions.custom.DatabaseCustomException;
import com.example.startingclubbackend.exceptions.custom.ResourceNotFoundCustomException;
import com.example.startingclubbackend.model.forum.ForumComment;
import com.example.startingclubbackend.model.user.User;
import com.example.startingclubbackend.repository.ForumCommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ForumCommentServiceImpl implements ForumCommentService{
    private final ForumCommentRepository forumCommentRepository ;
    private final CommentDTOMapper commentDTOMapper;

    public ForumCommentServiceImpl(ForumCommentRepository forumCommentRepository, CommentDTOMapper commentDTOMapper) {
        this.forumCommentRepository = forumCommentRepository;
        this.commentDTOMapper = commentDTOMapper;
    }

    public ResponseEntity<Object> postComment(final CommentDTO commentDTO) {
        ForumComment comment = setCommentFields(commentDTO) ;
        saveComment(comment); ;

        final CommentDTO commentResponse = commentDTOMapper.apply(comment) ;
        return new ResponseEntity<>(commentResponse , HttpStatus.CREATED) ;
    }

    @Override
    public ResponseEntity<Object> fetchAllComments() {
        final List <ForumComment> comments =forumCommentRepository.fetchAllComments() ;
        final List<CommentDTO> commentDTOLlist = comments.stream()
                .map(commentDTOMapper)
                .toList();
        return new ResponseEntity<>(commentDTOLlist , HttpStatus.OK) ;
    }

    @Override
    public ResponseEntity<Object> fetchCommentById(final Long commentId) {
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
        saveComment(reply) ;

        final CommentDTO replyResponse = commentDTOMapper.apply(reply) ;
        return new ResponseEntity<>(replyResponse , HttpStatus.CREATED) ;

    }

    @Override
    public ResponseEntity<Object> fetchAllRepliesByCommendId(final Long parentCommentId) {
        List<ForumComment> replies = forumCommentRepository.fetchAllRepliesByCommendId(parentCommentId) ;
        final List<CommentDTO> repliesDTOResponse = replies.
                stream().map(commentDTOMapper).
                toList() ;
        return new ResponseEntity<>(repliesDTOResponse , HttpStatus.OK) ;
    }
    @Override
    public ResponseEntity<Object> likeComment(final Long commentId) {
        final ForumComment comment = getCommentById(commentId) ;
        comment.setLikesCount(comment.getLikesCount()+ 1);
        saveComment(comment) ;

        return ResponseEntity.ok().body("Comment liked successfully") ;
    }

    @Override
    public ResponseEntity<Object> editComment(final CommentDTO commentDTO,final  Long commentId) {
        final ForumComment currentComment = getCommentById(commentId);
        currentComment.setContent(commentDTO.getContent());
        currentComment.setUpdatedAt(LocalDateTime.now());
        saveComment(currentComment);
        final CommentDTO commentResponse = commentDTOMapper.apply(currentComment);
        return new ResponseEntity<>(commentResponse, HttpStatus.OK);

    }
    @Transactional
    @Override
    public ResponseEntity<Object> deleteComment(final Long commentId) {
        final ForumComment currentComment = getCommentById(commentId) ;
        //log.info("current comment : "+ currentComment.getContent());
        if(!currentComment.getReplies().isEmpty()){
            List<ForumComment> replies = currentComment.getReplies() ;
            for (ForumComment reply :replies ){
                //log.info("reply content : "+reply.getContent());
                deleteComment(reply.getId()) ;
            }
        }
//        log.info("current comment has no replies");
//        log.info("this comment wil be deleted :"+ currentComment.getContent());
        deleteComment(currentComment);

        final String successResponse = String.format("comment with ID :  %d deleted successfully", currentComment.getId());
        return new ResponseEntity<>(successResponse , HttpStatus.OK);
    }
    @Override
    public void saveComment(final ForumComment comment) {
        try {
            forumCommentRepository.save(comment);
        }catch (ConstraintViolationException cve) {
            throw new DatabaseCustomException("A database constraint was violated when saving refresh token");
        }
    }

    @Override
    public void deleteComment(ForumComment comment) {
        try{
            forumCommentRepository.delete(comment);
        }catch (DataAccessException e) {
            // Handle general data access issues
            throw new DatabaseCustomException("An error occurred while trying to delete the comment.");
        }
    }

    private ForumComment setCommentFields(final CommentDTO commentDTO ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication() ;
        User user = (User) auth.getPrincipal();

        ForumComment comment = new ForumComment() ;
        comment.setContent(commentDTO.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        comment.setPostedBy(user);
        return comment ;
    }

    @Override
    public ForumComment getCommentById(final Long commentId) {
        return forumCommentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundCustomException("comment not found")) ;
    }

}
