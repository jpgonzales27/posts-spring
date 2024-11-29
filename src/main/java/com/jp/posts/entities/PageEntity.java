package com.jp.posts.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Page")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateCreation;

    @Column(unique = true)
    private String title;

    @OneToOne
    @JoinColumn(name = "id_user", unique = true)
    private UserEntity user;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_page")
    private List<PostEntity> posts = new ArrayList<>();

    public void addPost(PostEntity post){
        posts.add(post);
    }
    public void removePost(PostEntity post){
        posts.remove(post);
    }
}
