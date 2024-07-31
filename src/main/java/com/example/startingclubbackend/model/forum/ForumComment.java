package com.example.startingclubbackend.model.forum;

import com.example.startingclubbackend.model.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "forum_comments")
public class ForumComment {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id ;

    @Column(name = "content" , nullable = false)
    private String content ;

    @Column(name = "likes_count")
    private int likesCount = 0;

    @Column(name = "created_at" , nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at" , nullable = false)
    private LocalDateTime updatedAt ;

    @ManyToOne()
    @JoinColumn(name = "posted_by" , nullable = false)
    private User postedBy ;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="parent_comment_id")
    @JsonBackReference
    private ForumComment parentComment ; // parent of this comment, top level ones will have this field as null

    @OneToMany(fetch=FetchType.LAZY, mappedBy="parentComment")
    @OrderBy("id ASC")
    @JsonManagedReference
    private List<ForumComment> replies; // children of this comment

}
