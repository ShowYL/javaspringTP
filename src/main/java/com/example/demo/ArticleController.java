package com.example.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.controller.ArticleService;
import com.example.db.Article;
import com.example.request.ArticleRequest;
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

        Optional<Article> articleOptional = articleService.create(request.getUsername(),
                request.getPassword(), request.getContent());

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

        HashMap<String, String> data = new HashMap<String, String>();
        data.put("status", "success");
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/article/{id}")
    public @ResponseBody ResponseEntity<Object> get(@PathVariable String id) {
        Integer idInt;
        try {
            idInt = Integer.valueOf(id);
        } catch (NumberFormatException e) {
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("status", "failure");
            data.put("error", "invalid id");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        Optional<Article> articleOptional = articleService.get(idInt);

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
    public @ResponseBody ResponseEntity<Object> modify(@PathVariable String id, @RequestBody ArticleRequest request) {
        Integer idInt;
        try {
            idInt = Integer.valueOf(id);
        } catch (NumberFormatException e) {
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("status", "failure");
            data.put("error", "invalid id");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        Optional<Article> articleOptional = articleService.modify(idInt, request.getContent(), request.getPassword());

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

}
