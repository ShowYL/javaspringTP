package com.example.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostMapping("/user/create")
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

    @DeleteMapping("/user/delete")
    public @ResponseBody ResponseEntity<Object> deleteUser(@RequestParam String username,
            @RequestParam String password) {
        boolean success = userService.delete(username, password);

        if (!success) {
            return Utils.returnFailure();
        }

        return Utils.returnSuccess();
    }

    @GetMapping("/user")
    public @ResponseBody ResponseEntity<Object> getUser(@RequestParam String username, @RequestParam String password) {
        Optional<User> userOptional = userService.get(username, password);

        if (!userOptional.isPresent()) {
            return Utils.returnFailure();
        }

        Map<String, String> data = new HashMap<>();
        data.put("status", "success");
        data.put("username", userOptional.get().getUsername());
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

}
