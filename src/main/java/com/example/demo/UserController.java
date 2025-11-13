package com.example.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.controller.UserService;
import com.example.db.User;
import com.example.request.UserRequest;
import com.example.request.Utils;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/user/register")
    public @ResponseBody ResponseEntity<Object> createUser(@RequestBody UserRequest request) {
        boolean success = userService.create(request.username(), request.password(), request.role());

        if (!success) {
            return Utils.returnFailure();
        }

        Map<String, String> data = new HashMap<>();
        data.put("status", "success");
        data.put("username", request.username());
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @DeleteMapping("/user/delete/{username}")
    @PreAuthorize("#username == authentication.principal.username")
    public @ResponseBody ResponseEntity<Object> deleteUser(@PathVariable String username) {
        boolean success = userService.delete(username);

        if (!success) {
            return Utils.returnFailure();
        }

        return Utils.returnSuccess();
    }

    @GetMapping("/user/{username}")
    public @ResponseBody ResponseEntity<Object> getUser(@PathVariable String username) {
        Optional<User> userOptional = userService.get(username);

        if (!userOptional.isPresent()) {
            return Utils.returnFailure();
        }

        Map<String, String> data = new HashMap<>();
        data.put("status", "success");
        data.put("username", userOptional.get().getUsername());
        data.put("role", userOptional.get().getRole().name());
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
    
}
