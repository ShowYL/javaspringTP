package com.example.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.db.Article;
import com.example.db.ArticleRepository;
import com.example.db.User;

@Service
public class ArticleService {

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    UserService userService;

    public Optional<Article> create(String authorUsername, String authorPassword, String content) {
        Optional<User> userOptional = userService.get(authorUsername, authorPassword);

        if (!userOptional.isPresent()) {
            return Optional.empty();
        }

        Article article = new Article(userOptional.get(), content);
        articleRepository.save(article);
        return Optional.of(article);
    }

    public boolean delete(Integer articleId, String authorPassword) {
        Optional<Article> articleOptional = this.get(articleId);

        if (!articleOptional.isPresent()) {
            return false;
        }

        Article article = articleOptional.get();

        if (!article.getAuthor().getPassword().equals(authorPassword)) {
            return false;
        }

        articleRepository.delete(article);
        return true;
    }

    public Optional<Article> get(Integer id) {
        Optional<Article> articleOptional = articleRepository.findById(id);

        if (!articleOptional.isPresent()) {
            return Optional.empty();
        }

        return Optional.of(articleOptional.get());
    }

    public List<Article> get(String authorUsername){
        ArrayList<Article> list = this.getAll();
        return list.stream().filter(e -> e.getAuthor().getUsername().equals(authorUsername)).toList();
    }

    public ArrayList<Article> getAll() {
        Iterable<Article> articleIterable = articleRepository.findAll();
        ArrayList<Article> list = new ArrayList<Article>();
        articleIterable.forEach(list::add);
        return list;
    }

    public Optional<Article> modify(Integer id, String content, String authorPassword) {
        Optional<Article> articleOptional = articleRepository.findById(id);

        if (!articleOptional.isPresent()) {
            return Optional.empty();
        }

        Article article = articleOptional.get();

        if (!article.getAuthor().getPassword().equals(authorPassword)) {
            return Optional.empty();
        }

        article.setDate(LocalDateTime.now())
                .setContent(content);

        return Optional.of(articleRepository.save(article));
    }

    public boolean like(Integer id, String authorUsername, String authorPassword){
        Optional<User> userOptional = userService.get(authorUsername, authorPassword);

        if (!userOptional.isPresent()){
            return false;
        }

        Optional<Article> articleOptional = articleRepository.findById(id);

        if (!articleOptional.isPresent()){
            return false;
        }

        Article article = articleOptional.get();
        User user = userOptional.get();

        article.toggleLike(user);
        articleRepository.save(article);
        return true;
    }

    public boolean dislike(Integer id, String authorUsername, String authorPassword){
        Optional<User> userOptional = userService.get(authorUsername, authorPassword);

        if (!userOptional.isPresent()){
            return false;
        }

        Optional<Article> articleOptional = articleRepository.findById(id);

        if (!articleOptional.isPresent()){
            return false;
        }

        Article article = articleOptional.get();
        User user = userOptional.get();

        article.toggleDislike(user);
        articleRepository.save(article);
        return true;
    }

    public Optional<Integer> getLikesCount(Integer id){
        Optional<Article> articleOptional = articleRepository.findById(id);

        if (!articleOptional.isPresent()){
            return Optional.empty();
        }

        Article article = articleOptional.get();

        return Optional.of(article.getLikesCount());
    }

    public Optional<Integer> getDislikesCount(Integer id){
        Optional<Article> articleOptional = articleRepository.findById(id);

        if (!articleOptional.isPresent()){
            return Optional.empty();
        }

        Article article = articleOptional.get();

        return Optional.of(article.getDislikesCount());
    }
}
