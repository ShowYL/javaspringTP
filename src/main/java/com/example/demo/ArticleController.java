package com.example.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import com.example.controller.ArticleService;
import com.example.controller.UserService;
import com.example.db.Article;
import com.example.db.User;
import com.example.db.UserRole;
import com.example.request.ArticleRequest;
import com.example.request.Utils;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    private User getCurrentUser(UserDetails userDetails) {
        if (userDetails == null)
            return null;
        return userService.get(userDetails.getUsername()).orElse(null);
    }

    @PostMapping("/article/create")
    @PreAuthorize("hasAuthority('Publisher') or hasAuthority('Moderator')")
    public ResponseEntity<Object> create(@RequestBody ArticleRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = getCurrentUser(userDetails);
        return articleService.create(currentUser, request.content())
                .map(article -> {
                    HashMap<String, String> data = new HashMap<>();
                    data.put("status", "success");
                    data.put("id", article.getID().toString());
                    return new ResponseEntity<Object>(data, HttpStatus.OK);
                }).orElse(Utils.returnFailure());
    }

    @DeleteMapping("/article/{id}/delete")
    @PreAuthorize("hasAuthority('Moderator') or (hasAuthority('Publisher') and @articleService.get(#id).get().getAuthor().getUsername() == #userDetails.getUsername())")
    public ResponseEntity<Object> delete(@PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return articleService.delete(id) ? Utils.returnSuccess() : Utils.returnFailure();
    }

    @GetMapping("/article/{id}")
    public ResponseEntity<Object> get(@PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = getCurrentUser(userDetails);
        return articleService.get(id)
                .map(article -> new ResponseEntity<Object>(formatArticleResponse(article, currentUser), HttpStatus.OK))
                .orElse(Utils.returnFailure());
    }

    @PutMapping("article/{id}")
    @PreAuthorize("hasAuthority('Publisher') and @articleService.get(#id).get().getAuthor().getUsername() == #userDetails.getUsername()")
    public ResponseEntity<Object> modify(@PathVariable Integer id, @RequestBody ArticleRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return articleService.modify(id, request.content()).isPresent() ? Utils.returnSuccess() : Utils.returnFailure();
    }

    @GetMapping("/article")
    public ResponseEntity<Object> getAll(@AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = getCurrentUser(userDetails);
        ArrayList<Article> articles = articleService.getAll();
        ArrayList<Object> data = articles.stream()
                .map(article -> formatArticleResponse(article, currentUser))
                .collect(Collectors.toCollection(ArrayList::new));
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping("/article/{id}/like")
    public ResponseEntity<Object> like(@PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = getCurrentUser(userDetails);
        return articleService.like(id, currentUser) ? Utils.returnSuccess() : Utils.returnFailure();
    }

    @PostMapping("/article/{id}/dislike")
    public ResponseEntity<Object> dislike(@PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = getCurrentUser(userDetails);
        return articleService.dislike(id, currentUser) ? Utils.returnSuccess() : Utils.returnFailure();
    }

    @GetMapping("/article/{id}/like")
    public ResponseEntity<Object> getLikes(@PathVariable Integer id, @AuthenticationPrincipal UserDetails userDetails) {
        User user = getCurrentUser(userDetails);
        return articleService.getLikes(id)
                .map((Map<String, Object> likes) -> {
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("status", "success");
                    if (user.getRole() == UserRole.Moderator) {
                        data.putAll(likes);
                    } else {
                        Object count = likes.get("count");
                        data.put("count", count);
                    }
                    return new ResponseEntity<Object>(data, HttpStatus.OK);
                }).orElse(Utils.returnFailure());
    }

    @GetMapping("/article/{id}/dislike")
    public ResponseEntity<Object> getDislikes(@PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = getCurrentUser(userDetails);
        return articleService.getDislikes(id)
                .map(dislikes -> {
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("status", "success");
                    if (user.getRole() == UserRole.Moderator) {
                        data.putAll(dislikes);
                    } else {
                        Object count = dislikes.get("count");
                        data.put("count", count);
                    }
                    return new ResponseEntity<Object>(data, HttpStatus.OK);
                }).orElse(Utils.returnFailure());
    }

    private Map<String, Object> formatArticleResponse(Article article, User currentUser) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", article.getID());
        response.put("author", article.getAuthor().getUsername());
        response.put("date", article.getDate());
        response.put("content", article.getContent());

        if (currentUser == null) {
            // Unauthenticated user: Only base info
            return response;
        }

        if (currentUser.getRole() == UserRole.Moderator) {
            // Moderator: Full details
            response.put("likes", articleService.getLikes(article.getID()).get());
            response.put("dislikes", articleService.getDislikes(article.getID()).get());
        } else if (currentUser.getRole() == UserRole.Publisher) {
            response.put("total_likes", article.getLikesCount());
            response.put("total_dislikes", article.getDislikesCount());
        }
        return response;
    }
}
