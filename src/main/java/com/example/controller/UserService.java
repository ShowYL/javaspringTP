package com.example.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.db.User;
import com.example.db.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void create(String username, String password){
        userRepository.save(new User(username, password));
    }

    public boolean delete(String username, String password){
        Optional<User> userOptional = userRepository.findById(username);
        
        if (!userOptional.isPresent()){
            return false;
        }

        User user = userOptional.get();

        if (user.getPassword().equals(password)){
            userRepository.delete(user);
            return true;
        }

        return false;

    }

    public User get(String username, String password){
        Optional<User> userOptional = userRepository.findById(username);
        
        if (!userOptional.isPresent()){
            return null;
        }

        User user = userOptional.get();

        if (user.getPassword().equals(password)){
            return user;
        }

        return null;
    }
}
