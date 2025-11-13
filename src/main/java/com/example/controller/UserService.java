package com.example.controller;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean create(String username, String password, UserRole role) {
        if (userRepository.existsById(username)) {
            return false;
        }
        
        userRepository.save(new User(username, passwordEncoder.encode(password), role));
        return true;
    }

    public boolean delete(String username) {
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

        userRepository.delete(user);
        return true;
    }

    public Optional<User> get(String username) {
        return userRepository.findById(username);
    }
}
