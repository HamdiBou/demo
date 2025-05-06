package com.example.demo.service;

import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.model.User;

public interface UserService {
    UserDTO register(User user);
    String authenticate(LoginDTO loginDTO);
    String getUserIdFromToken(String token);
    UserDTO getCurrentUser(String userId);
    UserDTO updateUser(String userId, User user);
}