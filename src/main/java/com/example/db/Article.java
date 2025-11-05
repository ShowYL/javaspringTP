package com.example.db;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Article {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn
    private User author;

    private LocalDateTime date;

    private String content;

    public Article(User author, String content){
        this.author = author;
        this.content = content;
        this.date = LocalDateTime.now();
    }

    public Article(){}

    public Integer getID(){
        return this.id;
    }

    public User getAuthor(){
        return this.author;
    }

    public LocalDateTime getDate(){
        return this.date;
    }

    public String getContent(){
        return this.content;
    }

    public Article setAuthor(User username){
        this.author = username;
        return this;
    }

    public Article setDate(LocalDateTime date){
        this.date = date;
        return this;
    }

    public Article setContent(String content){
        this.content = content;
        return this;
    }

}
