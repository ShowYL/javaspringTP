package com.example.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.controller.ArticleService;
import com.example.db.Article;
import com.example.request.ArticleRequest;
import com.example.request.Like;
import com.example.request.Utils;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping("/article/create")
    public @ResponseBody ResponseEntity<Object> create(@RequestBody ArticleRequest request) {

        Optional<Article> articleOptional = articleService.create(request.username(),
                request.password(), request.content());

        if (!articleOptional.isPresent()) {
            return Utils.returnFailure();
        }

        Article article = articleOptional.get();
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("status", "success");
        data.put("id", article.getID().toString());
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @DeleteMapping("/article/delete")
    public @ResponseBody ResponseEntity<Object> delete(@RequestParam Integer id,
            @RequestParam String password) {

        boolean success = articleService.delete(id, password);

        if (!success) {
            return Utils.returnFailure();
        }

        return Utils.returnSuccess();
    }

    @GetMapping("/article/{id}")
    public @ResponseBody ResponseEntity<Object> get(@PathVariable Integer id) {
        Optional<Article> articleOptional = articleService.get(id);

        if (!articleOptional.isPresent()) {
            return Utils.returnFailure();
        }

        Article article = articleOptional.get();
        HashMap<String, Object> articleData = new HashMap<>();
        articleData.put("id", article.getID());
        articleData.put("content", article.getContent());
        articleData.put("date", article.getDate());
        articleData.put("author", article.getAuthor().getUsername());
        return new ResponseEntity<>(articleData, HttpStatus.OK);
    }

    @PutMapping("article/{id}")
    public @ResponseBody ResponseEntity<Object> modify(@PathVariable Integer id, @RequestBody ArticleRequest request) {
        Optional<Article> articleOptional = articleService.modify(id, request.content(), request.password());

        if (!articleOptional.isPresent()) {
            return Utils.returnFailure();
        }

        Article article = articleOptional.get();
        HashMap<String, Object> articleData = new HashMap<>();
        articleData.put("status", "success");
        articleData.put("id", article.getID());
        articleData.put("content", article.getContent());
        articleData.put("date", article.getDate());
        articleData.put("author", article.getAuthor().getUsername());
        return new ResponseEntity<>(articleData, HttpStatus.OK);
    }

    @GetMapping("/article")
    public @ResponseBody ResponseEntity<Object> getAll() {
        ArrayList<Article> articles = articleService.getAll();
        ArrayList<Object> data = new ArrayList<>();
        articles.forEach(e -> {
            HashMap<String, Object> articleData = new HashMap<>();
            articleData.put("id", e.getID());
            articleData.put("content", e.getContent());
            articleData.put("date", e.getDate());
            articleData.put("author", e.getAuthor().getUsername());

            data.add(articleData);
        });
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping("/article/{id}/like")
    public @ResponseBody ResponseEntity<Object> like(@PathVariable Integer id, @RequestBody Like request) {
        boolean success = articleService.like(id, request.username(), request.password());

        if (!success) {
            return Utils.returnFailure();
        }

        return Utils.returnSuccess();
    }

    @PostMapping("/article/{id}/dislike")
    public @ResponseBody ResponseEntity<Object> dislike(@PathVariable Integer id, @RequestBody Like request) {
        boolean success = articleService.dislike(id, request.username(), request.password());

        if (!success) {
            return Utils.returnFailure();
        }

        return Utils.returnSuccess();
    }

    @GetMapping("/article/{id}/like")
    public @ResponseBody ResponseEntity<Object> like(@PathVariable Integer id) {
        Optional<Map<String, Object>> likesOptional = articleService.getLikes(id);

        if (!likesOptional.isPresent()) {
            return Utils.returnFailure();
        }

        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("status", "success");
        data.putAll(likesOptional.get());

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/article/{id}/dislike")
    public @ResponseBody ResponseEntity<Object> dislike(@PathVariable Integer id) {
        Optional<Map<String, Object>> dislikesOptional = articleService.getDislikes(id);

        if (!dislikesOptional.isPresent()) {
            return Utils.returnFailure();
        }

        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("status", "success");
        data.putAll(dislikesOptional.get());

        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
