package com.example.startingclubbackend.repository;

import com.example.startingclubbackend.model.forum.ForumComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForumCommentRepository  extends JpaRepository<ForumComment, Long> {

}
