package com.example.startingclubbackend.repository;

import com.example.startingclubbackend.model.forum.ForumComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface ForumCommentRepository extends JpaRepository<ForumComment, Long> {

    @Query("SELECT r FROM ForumComment r where r.parentComment.id = :parentCommentId order by r.id desc  ")
    List<ForumComment> fetchAllRepliesByCommendId(@Param("parentCommentId") Long parentCommentId);

    @Query("SELECT r FROM ForumComment r where r.parentComment IS NULL  order by r.id desc")
    List<ForumComment> fetchAllComments();
}
