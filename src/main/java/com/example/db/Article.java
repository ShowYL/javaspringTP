package com.example.db;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn
    private User author;

    private LocalDateTime date;

    private String content;

    @ManyToMany
    @JoinTable(name = "article_likes", joinColumns = @JoinColumn(name = "article_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> likes;

    @ManyToMany
    @JoinTable(name = "article_dislikes", joinColumns = @JoinColumn(name = "article_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> dislikes;

    public Article(User author, String content) {
        this.author = author;
        this.content = content;
        this.date = LocalDateTime.now();
    }

    public Article() {
    }

    public Integer getID() {
        return this.id;
    }

    public User getAuthor() {
        return this.author;
    }

    public LocalDateTime getDate() {
        return this.date;
    }

    public String getContent() {
        return this.content;
    }

    public Set<User> getLikes() {
        return this.likes;
    }

    public Set<User> getDislikes() {
        return this.dislikes;
    }

    public Article setAuthor(User username) {
        this.author = username;
        return this;
    }

    public Article setDate(LocalDateTime date) {
        this.date = date;
        return this;
    }

    public Article setContent(String content) {
        this.content = content;
        return this;
    }

    public void toggleLike(User user) {
        if (this.likes.contains(user)) {
            this.likes.remove(user);
            return;
        }

        this.likes.add(user);
        this.dislikes.remove(user);
    }

    public void toggleDislike(User user) {
        if (this.dislikes.contains(user)) {
            this.dislikes.remove(user);
            return;
        }

        this.dislikes.add(user);
        this.likes.remove(user);
    }

    public Integer getLikesCount() {
        return this.likes.size();
    }

    public Integer getDislikesCount() {
        return this.dislikes.size();
    }
}
