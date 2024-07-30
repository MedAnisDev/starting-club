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

@Service
public class ForumCommentServiceImpl implements ForumCommentService{
    private final ForumCommentRepository forumCommentRepository ;
    private final CommentDTOMapper commentDTOMapper;

    public ForumCommentServiceImpl(ForumCommentRepository forumCommentRepository, CommentDTOMapper commentDTOMapper) {
        this.forumCommentRepository = forumCommentRepository;
        this.commentDTOMapper = commentDTOMapper;
    }

    public ResponseEntity<Object> postComment(final CommentDTO commentDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication() ;
        User user = (User) auth.getPrincipal();

        ForumComment comment = new ForumComment() ;
        comment.setContent(commentDTO.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setPostedBy(user);
        forumCommentRepository.save(comment) ;

        final CommentDTO commentResponse = commentDTOMapper.apply(comment) ;
        return new ResponseEntity<>(commentResponse , HttpStatus.CREATED) ;
    }


}
