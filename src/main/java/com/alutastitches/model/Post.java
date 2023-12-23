package com.alutastitches.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_generator")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "image")
    private String image;

    @Column(name = "description")
    private String description;

    @Column(name = "published")
    private boolean published;

    @Column(name = "postDate")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date postDate;

    @Column(name = "likes")
    private Long postLikes;
    public Post(String title, String image, String description, boolean published, Long postLikes) {
        this.title = title;
        this.image = image;
        this.description = description;
        this.published = published;
        this.postLikes = postLikes;

    }

    // Set likes Long value to 0  when creating a new post
    @PrePersist
    public void prePersist() {
            this.postLikes = 0L;

    }
    public void incrementPost(){
        postLikes++;
    }
    public void decrementPost(){
        postLikes--;
    }
}
