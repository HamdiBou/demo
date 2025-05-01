package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.service.UserService;
import com.example.demo.model.User;
import com.example.demo.dto.UserDTO;
import com.example.demo.dto.LoginDTO;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody User user) {
        UserDTO registeredUser = userService.register(user);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
public ResponseEntity<Map<String, String>> login(@RequestBody LoginDTO loginDTO) {
    String token = userService.authenticate(loginDTO);

    Map<String, String> response = new HashMap<>();
    response.put("token", token);

    return ResponseEntity.ok(response);
}


    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(@RequestAttribute("userId") String userId) {
        UserDTO user = userService.getCurrentUser(userId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/me")
    public ResponseEntity<UserDTO> updateUser(@RequestAttribute("userId") String userId, @RequestBody User user) {
        UserDTO updatedUser = userService.updateUser(userId, user);
        return ResponseEntity.ok(updatedUser);
    }
}

