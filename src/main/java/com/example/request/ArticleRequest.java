package com.example.request;

public class ArticleRequest {
    private String username;
    private String password;
    private String content;

    public ArticleRequest() {
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getContent() {
        return this.content;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setContent(String content) {
        this.content = content;
    }
}