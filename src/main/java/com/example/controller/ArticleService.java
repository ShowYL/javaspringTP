package com.example.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.db.Article;
import com.example.db.ArticleRepository;
import com.example.db.User;

@Service
public class ArticleService {

    @Autowired
    ArticleRepository articleRepository;

    // The author is passed in directly from the authenticated principal.
    public Optional<Article> create(User author, String content) {
        if (author == null) {
            return Optional.empty();
        }
        Article article = new Article(author, content);
        articleRepository.save(article);
        return Optional.of(article);
    }

    // Deletion logic is simplified. Authorization is handled in the controller.
    public boolean delete(Integer articleId) {
        if (!articleRepository.existsById(articleId)) {
            return false;
        }
        articleRepository.deleteById(articleId);
        return true;
    }

    public Optional<Article> get(Integer id) {
        return articleRepository.findById(id);
    }
    
    public List<Article> get(String authorUsername) {
        return StreamSupport.stream(articleRepository.findAll().spliterator(), false)
                .filter(a -> a.getAuthor().getUsername().equals(authorUsername))
                .collect(Collectors.toList());
    }
    

    public ArrayList<Article> getAll() {
        ArrayList<Article> list = new ArrayList<>();
        articleRepository.findAll().forEach(list::add);
        return list;
    }

    // Modification no longer requires a password. Authorization is handled in the controller.
    public Optional<Article> modify(Integer id, String content) {
        return articleRepository.findById(id).map(article -> {
            article.setDate(LocalDateTime.now()).setContent(content);
            return articleRepository.save(article);
        });
    }

    // The user who is liking/disliking is passed in directly.
    public boolean like(Integer id, User user) {
        if (user == null) return false;
        return articleRepository.findById(id).map(article -> {
            article.toggleLike(user);
            articleRepository.save(article);
            return true;
        }).orElse(false);
    }

    public boolean dislike(Integer id, User user) {
        if (user == null) return false;
        return articleRepository.findById(id).map(article -> {
            article.toggleDislike(user);
            articleRepository.save(article);
            return true;
        }).orElse(false);
    }

    // These methods now correctly format the data as specified.
    public Optional<Map<String, Object>> getLikes(Integer id) {
        return get(id).map(article -> Map.of(
                "count", article.getLikesCount(),
                "users", article.getLikes().stream().map(User::getUsername).collect(Collectors.toList())
        ));
    }

    public Optional<Map<String, Object>> getDislikes(Integer id) {
        return get(id).map(article -> Map.of(
                "count", article.getDislikesCount(),
                "users", article.getDislikes().stream().map(User::getUsername).collect(Collectors.toList())
        ));
    }
}
