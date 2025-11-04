package com.example.controller;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.db.Article;
import com.example.db.ArticleRepository;
import com.example.db.User;
import com.example.db.UserRepository;
import com.example.db.UserRole;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    public boolean create(String username, String password, UserRole role) {
        Optional<User> userOptional = userRepository.findById(username);

        if (userOptional.isPresent()) {
            return false;
        }

        userRepository.save(new User(username, password, role));
        return true;
    }

    public boolean delete(String username, String password) {
        Optional<User> userOptional = userRepository.findById(username);

        if (!userOptional.isPresent()) {
            return false;
        }

        User user = userOptional.get();

        ArrayList<Article> list = new ArrayList<Article>();
        articleRepository.findAll().forEach(list::add);
        boolean isThereAnArticleAttached = list.stream().filter(e -> e.getAuthor().getUsername().equals(username)).toList().size() != 0;

        if (isThereAnArticleAttached) {
            return false;
        }

        if (!user.getPassword().equals(password)) {
            return false;
        }

        userRepository.delete(user);
        return true;
    }

    public Optional<User> get(String username, String password) {
        Optional<User> userOptional = userRepository.findById(username);

        if (!userOptional.isPresent()) {
            return Optional.empty();
        }

        User user = userOptional.get();

        if (!user.getPassword().equals(password)) {
            return Optional.empty();
        }

        return Optional.of(user);
    }
}
