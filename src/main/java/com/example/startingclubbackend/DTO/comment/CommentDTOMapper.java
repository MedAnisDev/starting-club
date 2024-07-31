package com.example.startingclubbackend.DTO.comment;
import com.example.startingclubbackend.DTO.user.UserPublicDTOMapper;
import com.example.startingclubbackend.model.forum.ForumComment;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CommentDTOMapper implements Function<ForumComment ,CommentDTO> {

    private final UserPublicDTOMapper userPublicDTOMapper;

    public CommentDTOMapper(UserPublicDTOMapper userPublicDTOMapper) {
        this.userPublicDTOMapper = userPublicDTOMapper;
    }

    @Override
    public CommentDTO apply(ForumComment forumComment) {
        return new CommentDTO(
                forumComment.getId() ,
                forumComment.getContent() ,
                forumComment.getCreatedAt(),
                forumComment.getLikesCount(),
                userPublicDTOMapper.apply(forumComment.getPostedBy()),
                forumComment.getReplies()
                        .stream()
                        .map(this)
                        .toList()
        );
    }
}
