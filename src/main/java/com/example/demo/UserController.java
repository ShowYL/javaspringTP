package com.example.demo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.controller.UserService;
import com.example.db.User;

@RestController
@EnableJpaRepositories("com.example.db")
@EntityScan("com.example.db")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/user/create")
    public @ResponseBody ResponseEntity<Object> createUser(@RequestParam String username,
            @RequestParam String password) {
        userService.create(username, password);
        Map<String, String> data = new HashMap<>();
        data.put("status", "success");
        data.put("username", username);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @DeleteMapping("/user/delete")
    public @ResponseBody ResponseEntity<Object> deleteUser(@RequestParam String username,
            @RequestParam String password) {
        boolean success = userService.delete(username, password);

        if (success) {
            Map<String, String> data = new HashMap<>();
            data.put("status", "success");
            return new ResponseEntity<>(data, HttpStatus.OK);
        }

        Map<String, String> data = new HashMap<>();
        data.put("status", "failure");
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/user")
    public @ResponseBody ResponseEntity<Object> getUser(@RequestParam String username, @RequestParam String password) {
        User user = userService.get(username, password);

        if (user != null) {
            Map<String, String> data = new HashMap<>();
            data.put("status", "success");
            data.put("username", user.getUsername());
            return new ResponseEntity<>(data, HttpStatus.OK);
        }

        Map<String, String> data = new HashMap<>();
        data.put("status", "failure");
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

}
