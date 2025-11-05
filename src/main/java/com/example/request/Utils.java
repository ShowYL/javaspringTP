package com.example.request;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Utils {

    public static ResponseEntity<Object> returnFailure() {
        Map<String, String> data = new HashMap<>();
        data.put("status", "failure");
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<Object> returnSuccess() {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("status", "success");
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
